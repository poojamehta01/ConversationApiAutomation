package com.api.automation.utils;

import static com.api.automation.helpers.CommonTestHelper.getJsonString;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.InputStream;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
//import org.testng.Assert;

/**
 * Contains Wrapper for all RestAssured APIs which gives us back the response
 */
@Slf4j
public class RestAssuredUtils {

  private RestAssuredUtils() {
    throw new IllegalStateException("Object should not be created for RestAssuredUtils");
  }

  /**
   * Process any Api request based on the RequestBuilder passed in the param
   *
   * @param apiRequest, ApiBuilder Request to process the API
   * @return Response of the processed API
   */
  public static Response processApiRequest(ApiRequestBuilder apiRequest) {
    log.info("Processing the request!");
    log.info("Setting endPoint: " + apiRequest.getEndPoint());
    RestAssured.baseURI = apiRequest.getEndPoint();
    RequestSpecification specification = RestAssured.given();

    specification.urlEncodingEnabled(apiRequest.getUrlEncodingEnabled());

    if (Boolean.TRUE.equals(apiRequest.getEnableLogging())) {
      log.info("Logging Request!");
      specification.log().all();
    }

    if (!Objects.isNull(apiRequest.getRequestHeaders())) {
      log.info("Setting headers!");
      specification.headers(apiRequest.getRequestHeaders());
    }

    if (!Objects.isNull(apiRequest.getQueryParams())) {
      log.info("Setting Query Params: " + apiRequest.getQueryParams());
      specification.queryParams(apiRequest.getQueryParams());
    }

    boolean isRequestObjPassed = !Objects.isNull(apiRequest.getRequestBodyObj());
    boolean isRequestBodyPassed =
        !Objects.isNull(apiRequest.getRequestBody()) && !apiRequest.getRequestBody().isBlank();
    if (isRequestBodyPassed && isRequestObjPassed) {
      Assert.fail(
          "Both requestBodyObj and requestBody are passed. Only one of them is allowed in the request!");
    }

    String requestBody =
        isRequestObjPassed
            ? getJsonString(apiRequest.getRequestBodyObj())
            : apiRequest.getRequestBody();
    if (!Objects.isNull(requestBody) && !requestBody.isBlank()) {
      log.info("Setting Body: " + requestBody);
      specification.body(requestBody);
    }


    if (!Objects.isNull(apiRequest.getFormParams())) {
      log.info("Setting Form Params: " + apiRequest.getFormParams());
      specification.formParams(apiRequest.getFormParams());
    }

    if (Boolean.FALSE.equals(apiRequest.getAllowRedirects())) {
      log.info("Setting Allowed Redirects: " + apiRequest.getAllowRedirects());
      specification.redirects().follow(false);
    }

    if (Boolean.TRUE.equals(apiRequest.getEnableLogging())) {
      log.info("Logging Response!");
      specification.expect().log().all();
    }

    log.info("Sending the request");
    Response response = specification.request(apiRequest.getRequestMethod());

    if (apiRequest.getExpectedStatusCode() > 0) {
      Assert.assertEquals(
          response.getStatusCode(),
          apiRequest.getExpectedStatusCode(),
          "API status code doesn't match");
    }
    return response;
  }

  /**
   * Verifies schema based on the response and file name provided
   *
   * @param response, Response for which schema has to be verified
   * @param filePath, filePath of the schema
   */
  public static void verifySchema(Response response, String filePath) {
    InputStream inputStream = RestAssuredUtils.class.getResourceAsStream(filePath);
    assert inputStream != null;
    response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(inputStream));
    log.info("Schema verified");
  }
}
