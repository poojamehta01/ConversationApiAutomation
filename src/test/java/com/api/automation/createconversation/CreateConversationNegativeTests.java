package com.api.automation.createconversation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;
import static com.api.automation.constants.ConversationApiConstants.DISPLAY_NAME;
import static com.api.automation.constants.ConversationApiConstants.DUPLICATE_NAME;
import static com.api.automation.constants.ConversationApiConstants.IMAGE_URL;
import static com.api.automation.constants.ConversationApiConstants.NAME_ERROR;
import static com.api.automation.constants.ConversationApiConstants.TTL;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static io.restassured.http.Method.POST;

import com.api.automation.BaseTest;
import com.api.automation.enums.ErrorEnums;
import com.api.automation.helpers.ConversationApiHelper;
import com.api.automation.helpers.CreateConversationDataProviderHelper;
import com.api.automation.pojos.requests.CreateConversationRequest;
import com.api.automation.pojos.requests.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.requests.CreateConversationRequestIncorrectDataType;
import com.api.automation.pojos.requests.CreateConversationRequestIncorrectDataType.PropertiesObjInc;
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
@Epic("Epic name")
@Feature("Feature name")
@Story("Story name")
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

  @Test(description = "NC-create Conversation with NAME null ")
  public void createConversationNameNull() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name(null)
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

    softAssert.assertEquals(
        response.getDescription(), ErrorEnums.DATATYPE_INCORRECT.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.DATATYPE_INCORRECT.getCode());
    softAssert.assertEquals(
        response.getError().getName().get(0),
        ErrorEnums.DATATYPE_INCORRECT.getError().replace("$KEYNAME", "name"));
    softAssert.assertAll();
  }

  @Test(description = "NC-create Conversation duplicate NAME")
  public void createConversationDuplicateNameNegativeTc() {
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
    softAssert.assertEquals(response.getDescription(), ErrorEnums.DUPLICATE_NAME.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.DUPLICATE_NAME.getCode());
    softAssert.assertAll();
  }

  @Test(description = "NC-create Conversation DISPLAY_NAME Empty")
  public void createConversationDisplayNameEmpty() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name("")
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

    softAssert.assertEquals(
        response.getDescription(), ErrorEnums.EMPTY_DISPLAY_NAME.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.EMPTY_DISPLAY_NAME.getCode());
    softAssert.assertAll();
  }

  @Test(
      description = "NC-create Conversation with ImageUrl with whiteSpace, randomString, null",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationImageUrlIncorrect(String imageUrl) {
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
      softAssert.assertEquals(
          response.getDescription(), ErrorEnums.DATATYPE_INCORRECT.getDescription());
      softAssert.assertEquals(
          response.getError().getImage_url().get(0),
          ErrorEnums.DATATYPE_INCORRECT.getError().replace("$KEYNAME", "image_url"));
      softAssert.assertEquals(response.getCode(), ErrorEnums.DATATYPE_INCORRECT.getCode());
    } else {
      softAssert.assertEquals(response.getDescription(), ErrorEnums.INCORRECT_URL.getDescription());
      softAssert.assertEquals(response.getCode(), ErrorEnums.INCORRECT_URL.getCode());
    }
    softAssert.assertAll();
  }

  @Test(description = "NC-create Conversation with Properties null")
  public void createConversationPropertiesNull() {
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

    softAssert.assertEquals(
        response.getDescription(), ErrorEnums.PROPERTY_DATATYPE_INCORRECT.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.PROPERTY_DATATYPE_INCORRECT.getCode());

    softAssert.assertAll();
  }

  // todo
  @Test(description = "NC-create Conversation with Properties incorrectKey")
  public void createConversationPropertiesIncorrectKey() {
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

    softAssert.assertEquals(
        response.getDescription(), ErrorEnums.PROPERTY_DATATYPE_INCORRECT.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.PROPERTY_DATATYPE_INCORRECT.getCode());

    softAssert.assertAll();
  }

  @Test(
      description =
          "NC-create Conversation with name->int, display_name->int, imageUrl->int, in properties incorrectKey ")
  public void createConversationIncorrectDataType() {
    CreateConversationRequestIncorrectDataType createConversationRequest =
        CreateConversationRequestIncorrectDataType.builder()
            .name(1)
            .display_name(1)
            .image_url(1)
            .properties(PropertiesObjInc.builder().incorrectKey(1).build())
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
    softAssert.assertEquals(response.getCode(), ErrorEnums.DATATYPE_INCORRECT.getCode());
    softAssert.assertEquals(
        response.getDescription(), ErrorEnums.DATATYPE_INCORRECT.getDescription());
    softAssert.assertEquals(
        response.getError().getName().get(0),
        ErrorEnums.DATATYPE_INCORRECT.getError().replace("$KEYNAME", "name"));
    softAssert.assertEquals(
        response.getError().getDisplay_name().get(0),
        ErrorEnums.DATATYPE_INCORRECT.getError().replace("$KEYNAME", "display_name"));
    softAssert.assertEquals(
        response.getError().getImage_url().get(0),
        ErrorEnums.DATATYPE_INCORRECT.getError().replace("$KEYNAME", "image_url"));
    softAssert.assertEquals(
        response.getError().getTtl().get(0),
        ErrorEnums.TTL_DATATYPE_INCORRECT.getError().replace("$KEYNAME", "ttl"));

    softAssert.assertEquals(
        response.getError().getIncorrectKey().get(0), ErrorEnums.PROPERTY_INCORRECT_KEY.getError());

    softAssert.assertAll();
  }

  @Test(
      description = "NC-create Conversation with incorrect http method, other than post",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationIncorrectHttpMethod(Method method) {
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
    System.out.println("this is the response!!");
    System.out.println(response);
    softAssert.assertEquals(response.getCode(), ErrorEnums.INCORRECT_HTTP_METHODS.getCode());
    softAssert.assertEquals(
        response.getDescription(), ErrorEnums.INCORRECT_HTTP_METHODS.getDescription());

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

    softAssert.assertEquals(response.getDescription(), ErrorEnums.TOKEN_INVALID.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.TOKEN_INVALID.getCode());

    softAssert.assertAll();
  }

  // todo : token expired

}
