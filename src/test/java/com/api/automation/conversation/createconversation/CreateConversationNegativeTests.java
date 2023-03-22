package com.api.automation.conversation.createconversation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;
import static com.api.automation.constants.ConversationApiConstants.DISPLAY_NAME;
import static com.api.automation.constants.ConversationApiConstants.DUPLICATE_NAME;
import static com.api.automation.constants.ConversationApiConstants.IMAGE_URL;
import static com.api.automation.constants.ConversationApiConstants.NAME_ERROR;
import static com.api.automation.constants.ConversationApiConstants.TTL;
import static com.api.automation.enums.ErrorEnums.DATATYPE_INCORRECT;
import static com.api.automation.enums.ErrorEnums.DUPLICATE_NAME_ERROR;
import static com.api.automation.enums.ErrorEnums.EMPTY_DISPLAY_NAME;
import static com.api.automation.enums.ErrorEnums.INCORRECT_HTTP_METHODS;
import static com.api.automation.enums.ErrorEnums.INCORRECT_URL;
import static com.api.automation.enums.ErrorEnums.PROPERTY_DATATYPE_INCORRECT;
import static com.api.automation.enums.ErrorEnums.PROPERTY_INCORRECT_KEY;
import static com.api.automation.enums.ErrorEnums.TOKEN_EXPIRED;
import static com.api.automation.enums.ErrorEnums.TOKEN_INVALID;
import static com.api.automation.enums.ErrorEnums.TTL_DATATYPE_INCORRECT;
import static com.api.automation.enums.ErrorEnums.TTL_LESS_THAN_ZERO;
import static com.api.automation.enums.ErrorEnums.TTL_NOT_SAFE_NUM;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static io.restassured.http.Method.POST;

import com.api.automation.BaseTest;
import com.api.automation.enums.ErrorEnums;
import com.api.automation.helpers.ConversationApiHelper;
import com.api.automation.helpers.CreateConversationDataProviderHelper;
import com.api.automation.pojos.requests.CreateConversationRequest;
import com.api.automation.pojos.requests.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.requests.CreateConversationRequestIncorrectKey;
import com.api.automation.pojos.requests.CreateConversationRequestIncorrectKey.PropertiesObjIncorrect;
import com.api.automation.pojos.response.ErrorResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import com.vonage.jwt.Jwt;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.nio.file.Paths;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners({io.qameta.allure.testng.AllureTestNg.class})
@Epic("API Automation")
@Feature("Conversation Api")
@Story("Create Conversation Negative testCases")
public class CreateConversationNegativeTests extends BaseTest {

  private String jwtToken;

  @BeforeClass(alwaysRun = true)
  public void setup() {

    jwtToken =
        Jwt.builder()
            .applicationId(APP_ID)
            .privateKeyPath(Paths.get(PRIVATE_KEY))
            .build()
            .generate();
  }

  @Test(description = "NC-create Conversation with NAME null,boolean,number,double,array",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationNameInValid(Object name) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name(name)
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(1).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 400, createConversationRequest, jwtToken);
    Response errorResponse = RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = errorResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    assertValidationFail(response, DATATYPE_INCORRECT);
    softAssert.assertEquals(
        response.getError().getName().get(0),
        DATATYPE_INCORRECT.getError().replace("$KEYNAME", "name"));
    softAssert.assertAll();
  }

  @Test(description = "NC-create Conversation duplicate NAME")
  public void createConversationDuplicateName() {
    ConversationApiHelper.createConversation(jwtToken);
    PropertiesObj prop = PropertiesObj.builder().ttl(60).build();
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .display_name(DISPLAY_NAME)
            .name(DUPLICATE_NAME)
            .image_url(IMAGE_URL)
            .properties(prop)
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 400, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    assertValidationFail(response, DUPLICATE_NAME_ERROR);
    softAssert.assertAll();
  }

  @Test(description = "NC-create Conversation DISPLAY_NAME empty,boolean,number,double,array", dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationDisplayNameInvalid(Object displayName) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(displayName)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(TTL).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 400, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    if (displayName == "") {
      assertValidationFail(response, EMPTY_DISPLAY_NAME);
      softAssert.assertEquals(
          response.getError().getDisplay_name().get(0),
          EMPTY_DISPLAY_NAME.getError().replace("$KEYNAME", "display_name"));

    } else {
      assertValidationFail(response, DATATYPE_INCORRECT);
      softAssert.assertEquals(
          response.getError().getDisplay_name().get(0),
          DATATYPE_INCORRECT.getError().replace("$KEYNAME", "display_name"));

    }
    softAssert.assertAll();
  }

  @Test(
      description = "NC-create Conversation with ImageUrl with whiteSpace, randomString, null, empty,boolean,number,double,array",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationImageUrlInValid(Object imageUrl) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(DISPLAY_NAME)
            .image_url(imageUrl)
            .properties(PropertiesObj.builder().ttl(1).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 400, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();

    if (imageUrl == null) {
      assertValidationFail(response, DATATYPE_INCORRECT);
      softAssert.assertEquals(
          response.getError().getImage_url().get(0),
          DATATYPE_INCORRECT.getError().replace("$KEYNAME", "image_url"));
    } else {
      assertValidationFail(response, INCORRECT_URL);
    }
    softAssert.assertAll();
  }

  @Test(description = "NC-create Conversation with Properties null")
  public void createConversationPropertyNull() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(null)
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 400, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    assertValidationFail(response, PROPERTY_DATATYPE_INCORRECT);
    softAssert.assertAll();
  }

  @Test(
      description =
          "NC-create Conversation with incorrectKey in properties")
  public void createConversationPropertyWithIncorrectKey() {
    CreateConversationRequestIncorrectKey createConversationRequest =
        CreateConversationRequestIncorrectKey.builder()
            .name(NAME_ERROR)
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObjIncorrect.builder().incorrectKey(0).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 400, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    System.out.println("this is the response!!");
    System.out.println(response);
    assertValidationFail(response, PROPERTY_INCORRECT_KEY);
    softAssert.assertEquals(
        response.getError().getIncorrectKey().get(0), PROPERTY_INCORRECT_KEY.getError());

    softAssert.assertAll();
  }

  @Test(
      description = "NC-create Conversation with ttl -1, large int,null,whiteSpace",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationTTLInvalid(Object ttl) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(ttl).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 400, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();

    if (ttl == null) {
      assertValidationFail(response, TTL_DATATYPE_INCORRECT);
    } else if (ttl.equals(-1)) {
      assertValidationFail(response, TTL_LESS_THAN_ZERO);
    } else if (ttl.equals("10000000000000000")) {
      assertValidationFail(response, TTL_NOT_SAFE_NUM);
    } else {
      assertValidationFail(response, TTL_DATATYPE_INCORRECT);
    }
    softAssert.assertAll();
  }


  @Test(
      description = "NC-create Conversation with incorrect http method, other than post",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationHttpMethodInValid(Method method) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name(NAME_ERROR)
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(TTL).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            method, 405, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    assertValidationFail(response, INCORRECT_HTTP_METHODS);
    softAssert.assertAll();
  }

  @Test(description = "NC-create Conversation with invalidToken ")
  public void createConversationTokenInvalid() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(1).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(POST, 401, createConversationRequest, "");
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    assertValidationFail(response, TOKEN_INVALID);
    softAssert.assertAll();
  }

  @Test(description = "NC-create Conversation with tokenExpired ")
  public void createConversationTokenExpired() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(1).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST,
            401,
            createConversationRequest,
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2Nzk0Njc3NTgsImV4cCI6MTY3OTQ4OTM1OCwianRpIjoiQjJ3Zzc5cUFJQ2pBIiwiYXBwbGljYXRpb25faWQiOiI0OTYyZjQ3NC0xYzA3LTQxYmYtYmZmYi0xM2MzMjRhY2QzNGUiLCJzdWIiOiIiLCJhY2wiOiIifQ.TLNOFOYbM-9BcMV0D94Lu5OYw7hOKVcQbdHJppXvq7HTjsquOeHmcStctp1F-4byfTw2L6phkG_boXtatrVUR8W6YJqlHhoJ1bqOuWtCpa-ZiGhaez3j5MaKJ1qwfDSvH4N8QA3thF7OSEvhGzPcT_jthQHx5kY-_PP3jtkMavG6bmFlNMmEkrN5_47O2YxhO2FsAORn6kY1IDtPS6Lzxoqg9-OEc6UFFcdl5cexkmDNKGwJ-Cl69Vn4LXwVIvIucth9bAwgNG2U3ubVq6y-2oCKu10EbbwpbheG2sqqJAsHb9aLq_GxQTQkHBLaauxiWjD8bDhRd0TMh07GSLlexw");
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    assertValidationFail(response, TOKEN_EXPIRED);
    softAssert.assertAll();
  }

  public void assertValidationFail(ErrorResponse response, ErrorEnums enums) {
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(response.getDescription(), enums.getDescription());
    softAssert.assertEquals(response.getCode(), enums.getCode());
  }
}
