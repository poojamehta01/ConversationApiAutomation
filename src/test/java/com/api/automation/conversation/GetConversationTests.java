package com.api.automation.conversation.getconversation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;
import static com.api.automation.constants.conversation.CreateConversationConstants.DISPLAY_NAME;
import static com.api.automation.constants.conversation.CreateConversationConstants.IMAGE_URL;
import static com.api.automation.enums.conversation.CreateConversationEnums.GET_INVALID_CONVERSATION;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static com.api.automation.helpers.conversation.createconversation.CreateConversationApiHelper.createConversation;
import static io.restassured.http.Method.GET;

import com.api.automation.enums.conversation.CreateConversationEnums;
import com.api.automation.helpers.conversation.GetConversationApiHelper;
import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest;
import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.response.conversation.CreateConversationResponse;
import com.api.automation.pojos.response.conversation.ErrorResponse;
import com.api.automation.pojos.response.conversation.GetConversationApiResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import com.api.automation.utils.async.AsyncUtils;
import com.vonage.jwt.Jwt;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import java.nio.file.Paths;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Slf4j
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
@Epic("API Automation")
@Feature("Conversation Api")
@Story("Get Conversation Positive and Negative testCases")
public class GetConversationTests {

  private String jwtToken;
  Map<String, Object> responseMap;
  private CreateConversationResponse createConversationResponse;
  private CreateConversationRequest createConversationRequest;

  @BeforeClass(alwaysRun = true)
  public void setup() {

    jwtToken =
        Jwt.builder()
            .applicationId(APP_ID)
            .privateKeyPath(Paths.get(PRIVATE_KEY))
            .build()
            .generate();
  }


  @Test(description = "PC-get Conversation with valid conversationId with all the fields in createConversation Payload")
  public void getConversationWithConversationIdValid() {
    SoftAssert softAssert = new SoftAssert();
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .display_name(DISPLAY_NAME)
            .name("test_" + randomRegex("[A-Za-z0-9]{25}"))
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(60).build())
            .build();
    responseMap = createConversation(jwtToken, createConversationRequest);
    CreateConversationResponse conversationResponse = (CreateConversationResponse) responseMap.get("response");
    CreateConversationRequest conversationRequest = (CreateConversationRequest) responseMap.get("request");

    softAssert.assertNotNull(conversationResponse.getId());
    softAssert.assertNotNull(conversationResponse.getHref());
    ApiRequestBuilder getConversationBuilder =
        GetConversationApiHelper.getConversationBuilder(
            GET, 200, jwtToken, conversationResponse.getId());
    Response getConversationResponse = RestAssuredUtils.processApiRequest(getConversationBuilder);
    GetConversationApiResponse response =
        getConversationResponse.as(GetConversationApiResponse.class);
    softAssert.assertEquals(response.getUuid(), conversationResponse.getId());
    softAssert.assertEquals(response.getName(),conversationRequest.getName().toString());
    softAssert.assertEquals(response.getDisplay_name(),conversationRequest.getDisplay_name().toString());
    softAssert.assertEquals(response.getImage_url(),conversationRequest.getImage_url().toString());
    softAssert.assertEquals(response.getState(), "ACTIVE");
    softAssert.assertEquals(response.getProperties(),conversationRequest.getProperties().toString());
    softAssert.assertEquals(
        response.get_links().getSelf().getHref(), conversationResponse.getHref());
  }

  @Test(
      description = "NC-get Conversation with conversationId of conversation with ttl as 1 second")
  public void getConversationWithTTLOneSecond() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .display_name(DISPLAY_NAME)
            .name("test_" + randomRegex("[A-Za-z0-9]{25}"))
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(1).build())
            .build();
    responseMap = createConversation(jwtToken, createConversationRequest);
    SoftAssert softAssert = new SoftAssert();
    CreateConversationResponse conversationResponse = (CreateConversationResponse) responseMap.get("response");
    softAssert.assertNotNull(conversationResponse.getId());
    softAssert.assertNotNull(conversationResponse.getHref());
    ApiRequestBuilder getConversationBuilder =
        GetConversationApiHelper.getConversationBuilder(
            GET, 404, jwtToken, conversationResponse.getId());
    var lambdaContext =
        new Object() {
          Response getConversationResponse;
        };
    AsyncUtils.waitUntil(
        2,
        1,
        () -> {
          lambdaContext.getConversationResponse =
              RestAssuredUtils.processApiRequest(getConversationBuilder);
          return lambdaContext.getConversationResponse.getStatusCode() == 404;
        });

    ErrorResponse response = lambdaContext.getConversationResponse.as(ErrorResponse.class);

    assertValidationFail(response, GET_INVALID_CONVERSATION);
  }

  @DataProvider(name = "conversationIdNotFoundDataProvider")
  public Object[][] conversationIdNotFoundDataProvider() {
    return new Object[][] {{null}, {"xyz"}, {true}, {1}};
  }

  @Test(
      description = "NC-get Conversation with invalid conversationId-not found",
      dataProvider = "conversationIdNotFoundDataProvider")
  public void getConversationWithConversationIdNotFound(Object conversationId) {
    ApiRequestBuilder getConversationBuilder =
        GetConversationApiHelper.getConversationBuilder(GET, 404, jwtToken, conversationId);
    Response getConversationResponse = RestAssuredUtils.processApiRequest(getConversationBuilder);
    ErrorResponse response = getConversationResponse.as(ErrorResponse.class);
    assertValidationFail(response, GET_INVALID_CONVERSATION);
  }

  @DataProvider(name = "conversationIdInvalidDataProvider")
  public Object[][] conversationIdInvalidDataProvider() {
    return new Object[][] {{null},{true},{1.01}};
  }

  @Test(
      description = "NC-get Conversation with invalid conversationId-invalid",
      dataProvider = "conversationIdInvalidDataProvider")
  public void getConversationWithConversationIdInvalid(Object conversationId) {
    ApiRequestBuilder getConversationBuilder =
        GetConversationApiHelper.getConversationBuilder(GET, 404, jwtToken, conversationId);
    Response getConversationResponse = RestAssuredUtils.processApiRequest(getConversationBuilder);
    ErrorResponse response = getConversationResponse.as(ErrorResponse.class);
    assertValidationFail(response, GET_INVALID_CONVERSATION);
  }

  private void assertValidationFail(ErrorResponse response, CreateConversationEnums enums) {
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(response.getDescription(), enums.getDescription());
    softAssert.assertEquals(response.getCode(), enums.getCode());
  }
}
