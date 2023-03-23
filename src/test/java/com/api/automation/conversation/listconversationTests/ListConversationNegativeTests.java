package com.api.automation.conversation.listconversationTests;

import static com.api.automation.constants.CommonConstants.EXPIRED_TOKEN;
import static com.api.automation.constants.CommonConstants.INVALID_TOKEN;
import static com.api.automation.enums.conversation.CommonErrorEnums.INCORRECT_HTTP_METHODS;
import static com.api.automation.enums.conversation.InputValidationErrorEnums.DATATYPE_OTHER_THAN_INT;
import static com.api.automation.enums.conversation.InputValidationErrorEnums.DATE_FORMAT;
import static com.api.automation.enums.conversation.InputValidationErrorEnums.GREATER_THAN_EQUAL_TO_ZERO;
import static com.api.automation.enums.conversation.InputValidationErrorEnums.LESS_THAN_EQUAL_TO_1;
import static com.api.automation.enums.conversation.InputValidationErrorEnums.LESS_THAN_EQUAL_TO_100;
import static io.restassured.http.Method.DELETE;
import static io.restassured.http.Method.GET;
import static io.restassured.http.Method.PATCH;
import static io.restassured.http.Method.PUT;

import com.api.automation.helpers.conversation.CommonConversationHelper;
import com.api.automation.helpers.conversation.ListConversationApiHelper;
import com.api.automation.pojos.response.conversation.ErrorResponse;
import com.api.automation.pojos.response.conversation.ListConversationResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.util.HashMap;
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
@Story("List Conversation Negative testCases")
public class ListConversationNegativeTests {

  private String jwtToken;
  Map<String, Object> responseMap;
  public CommonConversationHelper commonConversationHelper;

  @BeforeClass(alwaysRun = true)
  public void setup() {

    jwtToken = CommonConversationHelper.generateJwtToken();
  }

  @Test(
      description = "NC-List conversation with start filter invalid",
      dataProvider = "listConversationFilterByStartDateInvalid")
  public void listConversationFilterByStartDateInvalid(Object date) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("date_start", date);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 400, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ErrorResponse response = listConversationResponse.as(ErrorResponse.class);
    commonConversationHelper.assertInputValidationFail(response, DATE_FORMAT);
    softAssert.assertEquals(
        response.getError().getDate_start().get(0),
        DATE_FORMAT.getError().replace("$KEYNAME", "date_start"));

    softAssert.assertAll();
  }

  @Test(description = "NC-List conversation with start>end date filter invalid")
  public void listConversationFilterByStartEndDateInvalid() {
    Object startDate = commonConversationHelper.generateDate(2023, 1, 1);
    Object endDate = commonConversationHelper.generateDate(2022, 1, 1);

    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("date_start", startDate);
    queryParams.put("date_end", endDate);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 200, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ListConversationResponse response = listConversationResponse.as(ListConversationResponse.class);
    softAssert.assertEquals(response.getCount(), 0);
    softAssert.assertAll();
  }

  @Test(
      description = "NC-List conversation with end filter invalid",
      dataProvider = "listConversationDateInvalidDataProvider")
  public void listConversationFilterByEndDateInvalid(Object date) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("date_end", date);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 400, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ErrorResponse response = listConversationResponse.as(ErrorResponse.class);
    commonConversationHelper.assertInputValidationFail(response, DATE_FORMAT);
    softAssert.assertEquals(
        response.getError().getDate_end().get(0),
        DATE_FORMAT.getError().replace("$KEYNAME", "date_end"));

    softAssert.assertAll();
  }

  @DataProvider(name = "listConversationPageSizeInvalidDataProvider")
  public Object[][] listConversationPageSizeInvalidDataProvider() {
    return new Object[][] {{0}, {-1}, {1.19}, {101}, {true}, {"test"}, {null}};
  }

  @Test(
      description = "NC-List conversation with pageSize filter invalid",
      dataProvider = "listConversationPageInvalidDataProvider")
  public void listConversationFilterByPageSizeInValid(Object pageSize) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("page_size", pageSize);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 400, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ErrorResponse response = listConversationResponse.as(ErrorResponse.class);
    if (pageSize == null || pageSize.getClass() == String.class || pageSize.equals(true)) {
      commonConversationHelper.assertInputValidationFail(response, DATATYPE_OTHER_THAN_INT);
      softAssert.assertEquals(
          response.getError().getPage_size().get(0),
          DATATYPE_OTHER_THAN_INT.getError().replace("$KEYNAME", "page_size"));

    } else if (Integer.parseInt(String.valueOf(pageSize)) <= 0) {
      commonConversationHelper.assertInputValidationFail(response, LESS_THAN_EQUAL_TO_1);
      softAssert.assertEquals(
          response.getError().getPage_size().get(0),
          LESS_THAN_EQUAL_TO_1.getError().replace("$KEYNAME", "page_size"));
    } else if (Integer.parseInt(String.valueOf(pageSize)) > 100) {
      commonConversationHelper.assertInputValidationFail(response, LESS_THAN_EQUAL_TO_100);
      softAssert.assertEquals(
          response.getError().getPage_size().get(0),
          LESS_THAN_EQUAL_TO_100.getError().replace("$KEYNAME", "page_size"));
    } else {
      commonConversationHelper.assertInputValidationFail(response, DATATYPE_OTHER_THAN_INT);
      softAssert.assertEquals(
          response.getError().getPage_size().get(0),
          DATATYPE_OTHER_THAN_INT.getError().replace("$KEYNAME", "page_size"));
    }

    softAssert.assertAll();
  }

  @DataProvider(name = "listConversationFilterRecordInValid")
  public Object[][] listConversationFilterRecordInValid() {
    return new Object[][] {{-1}, {"test"}, {true}, {null}};
  }

  @Test(
      description = "NC-List conversation with record Index filter invalid",
      dataProvider = "listConversationFilterRecordInValid")
  public void listConversationFilterByRecordIndexInValid(Object recordIndex) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("record_index", recordIndex);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 400, jwtToken, queryParams);

    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ErrorResponse response = listConversationResponse.as(ErrorResponse.class);
    if (recordIndex == null || recordIndex.getClass() == String.class || recordIndex.equals(true)) {
      commonConversationHelper.assertInputValidationFail(response, DATATYPE_OTHER_THAN_INT);
      softAssert.assertEquals(
          response.getError().getRecord_index().get(0),
          DATATYPE_OTHER_THAN_INT.getError().replace("$KEYNAME", "record_index"));

    } else if (Integer.parseInt(String.valueOf(recordIndex)) <= 0) {
      commonConversationHelper.assertInputValidationFail(response, GREATER_THAN_EQUAL_TO_ZERO);
      softAssert.assertEquals(
          response.getError().getRecord_index().get(0),
          GREATER_THAN_EQUAL_TO_ZERO.getError().replace("$KEYNAME", "record_index"));
    } else {
      commonConversationHelper.assertInputValidationFail(response, DATATYPE_OTHER_THAN_INT);
      softAssert.assertEquals(
          response.getError().getRecord_index().get(0),
          DATATYPE_OTHER_THAN_INT.getError().replace("$KEYNAME", "record_index"));
    }
    softAssert.assertAll();
  }

  @DataProvider(name = "listConversationFilterByOrderInvalid")
  public Object[][] listConversationFilterByOrderInvalid() {
    return new Object[][] {{"test"}, {"ASC1"}, {""}, {null}, {true}, {0}};
  }

  @Test(
      description = "NC-List conversation with order filter valid",
      dataProvider = "listConversationFilterByOrderInvalid")
  public void listConversationFilterByOrderInvalid(Object order) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("order", order);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 400, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ErrorResponse response = listConversationResponse.as(ErrorResponse.class);
    // assertPositiveValidation(response,10, 0);
    softAssert.assertAll();
  }

  @DataProvider(name = "listConversationFilterHTTPMethodIncorrect")
  public Object[][] listConversationFilterHTTPMethodIncorrect() {
    return new Object[][] {{DELETE}, {PUT}, {PATCH}};
  }

  @Test(
      description = "NC-List conversation with incorrect http method",
      dataProvider = "listConversationFilterHTTPMethodIncorrect")
  public void listConversationHttpInvalid(Method method) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("date_end", commonConversationHelper.generateDate(2023, 1, 1));
    queryParams.put("date_start", commonConversationHelper.generateDate(2024, 1, 1));
    queryParams.put("page_size", 10);
    queryParams.put("record_index", 10);
    queryParams.put("order", "ASC");
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(method, 405, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ErrorResponse response = listConversationResponse.as(ErrorResponse.class);
    commonConversationHelper.assertErrorValidation(response, INCORRECT_HTTP_METHODS);

    softAssert.assertAll();
  }

  @DataProvider(name = "listConversationFilterTokenInvalid")
  public Object[][] listConversationFilterTokenInvalid() {
    return new Object[][] {{INVALID_TOKEN}, {EXPIRED_TOKEN}};
  }
}
