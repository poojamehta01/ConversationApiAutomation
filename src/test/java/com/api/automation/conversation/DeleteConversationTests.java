package com.api.automation.conversation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.EXPIRED_TOKEN;
import static com.api.automation.constants.CommonConstants.INVALID_TOKEN;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;
import static com.api.automation.constants.conversation.CreateConversationConstants.DISPLAY_NAME;
import static com.api.automation.constants.conversation.CreateConversationConstants.IMAGE_URL;
import static com.api.automation.enums.conversation.CreateConversationEnums.EMPTY_CONVERSATION_ID;
import static com.api.automation.enums.conversation.CreateConversationEnums.INCORRECT_HTTP_METHODS;
import static com.api.automation.enums.conversation.CreateConversationEnums.INVALID_CONVERSATION_ID;
import static com.api.automation.enums.conversation.CreateConversationEnums.TOKEN_EXPIRED_ERROR;
import static com.api.automation.enums.conversation.CreateConversationEnums.TOKEN_INVALID_ERROR;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static com.api.automation.helpers.conversation.createconversation.CreateConversationApiHelper.createConversation;
import static io.restassured.http.Method.DELETE;
import static io.restassured.http.Method.POST;

import com.api.automation.enums.conversation.CreateConversationEnums;
import com.api.automation.helpers.conversation.DeleteConversationApiHelper;
import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest;
import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.response.conversation.CreateConversationResponse;
import com.api.automation.pojos.response.conversation.ErrorResponse;
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
@Story("Delete Conversation Positive and Negative testCases")
public class DeleteConversationTests {

  private String jwtToken;
  Map<String, Object> responseMap;

  @BeforeClass(alwaysRun = true)
  public void setup() {

    jwtToken =
        Jwt.builder()
            .applicationId(APP_ID)
            .privateKeyPath(Paths.get(PRIVATE_KEY))
            .build()
            .generate();
  }

  @Test(
      description =
          "PC-Delete Conversation with valid conversationId with all the fields in createConversation Payload")
  public void deleteConversationWithConversationIdValid() {
    SoftAssert softAssert = new SoftAssert();
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .display_name(DISPLAY_NAME)
            .name("test_" + randomRegex("[A-Za-z0-9]{25}"))
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(60).build())
            .build();
    responseMap = createConversation(jwtToken, createConversationRequest);
    CreateConversationResponse conversationResponse =
        (CreateConversationResponse) responseMap.get("response");

    softAssert.assertNotNull(conversationResponse.getId());
    softAssert.assertNotNull(conversationResponse.getHref());
    ApiRequestBuilder deleteConversationBuilder =
        DeleteConversationApiHelper.deleteConversationBuilder(
            DELETE, 200, jwtToken, conversationResponse.getId());
    Response deleteConversationResponse =
        RestAssuredUtils.processApiRequest(deleteConversationBuilder);
    softAssert.assertTrue(deleteConversationResponse.getBody().equals("{}"));
  }

  @Test(description = "NC-delete Conversation with conversationId empty")
  public void deleteConversationWithIdEmptyId() {
    SoftAssert softAssert = new SoftAssert();
    ApiRequestBuilder deleteConversationBuilder =
        DeleteConversationApiHelper.deleteConversationBuilder(DELETE, 400, jwtToken, "");
    Response deleteConversationResponse =
        RestAssuredUtils.processApiRequest(deleteConversationBuilder);
    ErrorResponse response = deleteConversationResponse.as(ErrorResponse.class);
    assertValidationFail(response, EMPTY_CONVERSATION_ID);
    softAssert.assertEquals(
        response.getError().getConversation_id().get(0), EMPTY_CONVERSATION_ID.getError());

    softAssert.assertAll();
  }

  @Test(
      description =
          "NC-Delete Conversation with conversationId of conversation with ttl as 1 second")
  public void deleteConversationWithTTLOneSecond() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .display_name(DISPLAY_NAME)
            .name("test_" + randomRegex("[A-Za-z0-9]{25}"))
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(1).build())
            .build();
    responseMap = createConversation(jwtToken, createConversationRequest);
    SoftAssert softAssert = new SoftAssert();
    CreateConversationResponse conversationResponse =
        (CreateConversationResponse) responseMap.get("response");
    softAssert.assertNotNull(conversationResponse.getId());
    softAssert.assertNotNull(conversationResponse.getHref());
    ApiRequestBuilder deleteConversationBuilder =
        DeleteConversationApiHelper.deleteConversationBuilder(
            DELETE, 404, jwtToken, conversationResponse.getId());
    var lambdaContext =
        new Object() {
          Response deleteConversationResponse;
        };
    AsyncUtils.waitUntil(
        5,
        1,
        () -> {
          lambdaContext.deleteConversationResponse =
              RestAssuredUtils.processApiRequest(deleteConversationBuilder);
          return lambdaContext.deleteConversationResponse.getStatusCode() == 404;
        });

    ErrorResponse response = lambdaContext.deleteConversationResponse.as(ErrorResponse.class);

    assertValidationFail(response, INVALID_CONVERSATION_ID);
  }

  @DataProvider(name = "conversationIdNotFoundDataProvider")
  public Object[][] conversationIdNotFoundDataProvider() {
    return new Object[][] {{null}, {"xyz"}, {true}, {1}, {1.01}};
  }

  @Test(
      description = "NC-delete Conversation with invalid conversationId-not found",
      dataProvider = "conversationIdNotFoundDataProvider")
  public void deleteConversationWithConversationIdNotFound(Object conversationId) {
    ApiRequestBuilder deleteConversationBuilder =
        DeleteConversationApiHelper.deleteConversationBuilder(
            DELETE, 404, jwtToken, conversationId);
    Response deleteConversationResponse =
        RestAssuredUtils.processApiRequest(deleteConversationBuilder);
    ErrorResponse response = deleteConversationResponse.as(ErrorResponse.class);
    assertValidationFail(response, INVALID_CONVERSATION_ID);
  }

  @Test(description = "NC-delete Conversation with invalid conversationId-not found")
  public void deleteConversationWithIncorrectHttpMethodPOST() {
    ApiRequestBuilder deleteConversationBuilder =
        DeleteConversationApiHelper.deleteConversationBuilder(POST, 405, jwtToken, "test");
    Response deleteConversationResponse =
        RestAssuredUtils.processApiRequest(deleteConversationBuilder);
    ErrorResponse response = deleteConversationResponse.as(ErrorResponse.class);
    assertValidationFail(response, INCORRECT_HTTP_METHODS);
  }

  @Test(description = "NC-Delete Conversation with invalidToken")
  public void getConversationTokenInvalid() {
    ApiRequestBuilder deleteConversationBuilder =
        DeleteConversationApiHelper.deleteConversationBuilder(DELETE, 401, INVALID_TOKEN, "test");
    Response deleteConversationResponse =
        RestAssuredUtils.processApiRequest(deleteConversationBuilder);
    ErrorResponse response = deleteConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    assertValidationFail(response, TOKEN_INVALID_ERROR);
    softAssert.assertAll();
  }

  @Test(description = "NC-delete Conversation with tokenExpired")
  public void getConversationTokenExpired() {
    ApiRequestBuilder deleteConversationBuilder =
        DeleteConversationApiHelper.deleteConversationBuilder(DELETE, 401, EXPIRED_TOKEN, "test");
    Response deleteConversationResponse =
        RestAssuredUtils.processApiRequest(deleteConversationBuilder);
    ErrorResponse response = deleteConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    assertValidationFail(response, TOKEN_EXPIRED_ERROR);
    softAssert.assertAll();
  }

  private void assertValidationFail(ErrorResponse response, CreateConversationEnums enums) {
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(response.getDescription(), enums.getDescription());
    softAssert.assertEquals(response.getCode(), enums.getCode());
    softAssert.assertAll();
  }
}
