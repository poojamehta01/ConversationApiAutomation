package com.api.automation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;
import static com.api.automation.constants.ConversationApiConstants.DISPLAY_NAME;
import static com.api.automation.constants.ConversationApiConstants.IMAGE_URL;
import static com.api.automation.constants.ConversationApiConstants.DUPLICATE_NAME;
import static com.api.automation.constants.ConversationApiConstants.TTL;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static io.restassured.http.Method.POST;

import com.api.automation.enums.ErrorEnums;
import com.api.automation.helpers.ConversationApiHelper;
import com.api.automation.helpers.CreateConversationDataProviderHelper;
import com.api.automation.pojos.requests.CreateConversationRequest;
import com.api.automation.pojos.requests.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.response.CreateConversationResponse;
import com.api.automation.pojos.response.ErrorResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import com.vonage.jwt.Jwt;
import io.restassured.response.Response;
import java.nio.file.Paths;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ConversationApiTests extends BaseTest {

  private String jwtToken;

  @BeforeClass(alwaysRun = true)
  public void setup() {

    jwtToken = Jwt.builder()
        .applicationId(APP_ID)
        .privateKeyPath(Paths.get(PRIVATE_KEY))
        .build()
        .generate();
  }

  @Test(description = "PC-create Conversation with name,display,imageUrl,properties")
  public void createConversation() {

    System.out.println("this is token in test");
    PropertiesObj prop = PropertiesObj.builder().ttl(TTL).build();
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .display_name(DISPLAY_NAME)
        .name("test_" + randomRegex("[a-z0-9]{21}"))
        .image_url(IMAGE_URL)
        .properties(prop)
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        200, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    CreateConversationResponse response = createConversationResponse.as(
        CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response.getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();
  }

  @Test(description = "PC-create Conversation with emptyBody")
  public void createConversationWithEmptyPayload() {

    System.out.println("this is token in test");
    PropertiesObj prop = PropertiesObj.builder().ttl(TTL).build();

    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        200, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    CreateConversationResponse response = createConversationResponse.as(
        CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response.getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();
  }

  @Test(description = "PC-create Conversation with all names emptyString,threeSpacesString,1000charName positive testcases", dataProvider = "conversationApi_dataProvider", dataProviderClass = CreateConversationDataProviderHelper.class)

  public void createConversationNamePositiveTc(String name) {
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .name(name)
        .display_name(DISPLAY_NAME)
        .image_url(IMAGE_URL)
        .properties(PropertiesObj.builder().ttl(TTL).build())
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        200, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    CreateConversationResponse response = createConversationResponse.as(
        CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response.getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();
  }


  @Test(description = "NC-create Conversation with NAME null ")

  public void createConversationNameNull() {
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .name(null)
        .display_name(DISPLAY_NAME)
        .image_url(IMAGE_URL)
        .properties(PropertiesObj.builder().ttl(1).build())
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        400, createConversationRequest, jwtToken);
    Response errorResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    ErrorResponse response = errorResponse.as(ErrorResponse.class);

    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(response.getDescription(),
        ErrorEnums.NULL_NAME.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.NULL_NAME.getCode());
    softAssert.assertEquals(response.getError().getName().get(0), ErrorEnums.NULL_NAME.getError());
    softAssert.assertAll();

  }

  @Test(description = "NC-create Conversation duplicate NAME")

  public void createConversationDuplicateNameNegativeTc() {
    ConversationApiHelper.createConversation(jwtToken);
    PropertiesObj prop = PropertiesObj.builder().ttl(60).build();
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .display_name(DISPLAY_NAME)
        .name(DUPLICATE_NAME)
        .image_url(IMAGE_URL)
        .properties(prop)
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        400, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(
        ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(response.getDescription(),
        ErrorEnums.DUPLICATE_NAME.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.DUPLICATE_NAME.getCode());
    softAssert.assertAll();
  }

  @Test(description = "PC-create Conversation DISPLAY_NAME positive testcases", dataProvider = "conversationApi_dataProvider", dataProviderClass = CreateConversationDataProviderHelper.class)

  public void createConversationDisplayNamePositiveTc(String displayName) {
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .name("test_" + randomRegex("[a-z0-9]{21}"))
        .display_name(displayName)
        .image_url(IMAGE_URL)
        .properties(PropertiesObj.builder().ttl(TTL).build())
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        200, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    CreateConversationResponse response = createConversationResponse.as(
        CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response.getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
  }

  @Test(description = "NC-create Conversation DISPLAY_NAME Empty")

  public void createConversationDisplayNameEmpty() {
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .name("test_" + randomRegex("[a-z0-9]{21}"))
        .display_name("")
        .image_url(IMAGE_URL)
        .properties(PropertiesObj.builder().ttl(TTL).build())
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        400, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(
        ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(response.getDescription(),
        ErrorEnums.EMPTY_DISPLAY_NAME.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.EMPTY_DISPLAY_NAME.getCode());
    softAssert.assertAll();
  }

  @Test(description = "PC-create Conversation with ImageUrl Empty ")

  public void createConversationImageUrlEmpty() {
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .name("test_" + randomRegex("[a-z0-9]{21}"))
        .display_name(DISPLAY_NAME)
        .image_url("")
        .properties(PropertiesObj.builder().ttl(1).build())
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        200, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    CreateConversationResponse response = createConversationResponse.as(
        CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response.getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();

  }

  @Test(description = "NC-create Conversation with ImageUrl with whiteSpace, randomString, null", dataProvider = "conversationApi_dataProvider", dataProviderClass = CreateConversationDataProviderHelper.class)

  public void createConversationImageUrlIncorrect(String imageUrl) {
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .name("test_" + randomRegex("[a-z0-9]{21}"))
        .display_name(DISPLAY_NAME)
        .image_url(imageUrl)
        .properties(PropertiesObj.builder().ttl(1).build())
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        400, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(
        ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();

    if (imageUrl == null) {
      softAssert.assertEquals(response.getDescription(),
          ErrorEnums.NULL_URL.getDescription());
      softAssert.assertEquals(response.getCode(), ErrorEnums.NULL_URL.getCode());
    } else {
      softAssert.assertEquals(response.getDescription(),
          ErrorEnums.INCORRECT_URL.getDescription());
      softAssert.assertEquals(response.getCode(), ErrorEnums.INCORRECT_URL.getCode());
    }
    softAssert.assertAll();

  }

  @Test(description = "NC-create Conversation with Properties null",dataProvider = "conversationApi_dataProvider", dataProviderClass = CreateConversationDataProviderHelper.class)

  public void createConversationPropertiesIncorrect() {
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .name("test_" + randomRegex("[a-z0-9]{21}"))
        .display_name(DISPLAY_NAME)
        .image_url(IMAGE_URL)
        .properties(null)
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        400, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(
        ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(response.getDescription(),
        ErrorEnums.PROPERTY_URL.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.PROPERTY_URL.getCode());

    softAssert.assertAll();

  }

  // can be a string, float allowed 1.00,1.34344
  @Test(description = "NC-create Conversation with Properties->TTL null, extra key-value, negative number, not a safeNumber ")

  public void createConversationTTLIncorrect() {
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .name("test_" + randomRegex("[a-z0-9]{21}"))
        .display_name(DISPLAY_NAME)
        .image_url(IMAGE_URL)
        .properties(null)
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversationBuilder(
        POST,
        400, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    ErrorResponse response = createConversationResponse.as(
        ErrorResponse.class);
    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(response.getDescription(),
        ErrorEnums.PROPERTY_URL.getDescription());
    softAssert.assertEquals(response.getCode(), ErrorEnums.PROPERTY_URL.getCode());

    softAssert.assertAll();

  }

}
