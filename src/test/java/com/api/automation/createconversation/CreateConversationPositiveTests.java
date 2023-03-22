package com.api.automation.createconversation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;
import static com.api.automation.constants.ConversationApiConstants.DISPLAY_NAME;
import static com.api.automation.constants.ConversationApiConstants.IMAGE_URL;
import static com.api.automation.constants.ConversationApiConstants.TTL;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static io.restassured.http.Method.POST;

import com.api.automation.BaseTest;
import com.api.automation.helpers.ConversationApiHelper;
import com.api.automation.helpers.CreateConversationDataProviderHelper;
import com.api.automation.pojos.requests.CreateConversationRequest;
import com.api.automation.pojos.requests.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.response.CreateConversationResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import com.vonage.jwt.Jwt;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
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
public class CreateConversationPositiveTests extends BaseTest {

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

  @Test(description = "PC-create Conversation with name,display,imageUrl,properties")
  public void createConversation() {

    System.out.println("this is token in test");
    PropertiesObj prop = PropertiesObj.builder().ttl(TTL).build();
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .display_name(DISPLAY_NAME)
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .image_url(IMAGE_URL)
            .properties(prop)
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response
            .getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();
  }

  @Test(description = "PC-create Conversation with emptyBody")
  public void createConversationWithEmptyPayload() {

    System.out.println("this is token in test");
    PropertiesObj prop = PropertiesObj.builder().ttl(TTL).build();

    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(POST, 200, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response
            .getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();
  }

  @Test(
      description =
          "PC-create Conversation with all names emptyString,threeSpacesString,1000charName positive testcases",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationNamePositiveTc(String name) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name(name)
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(TTL).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response
            .getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();
  }

  @Test(
      description = "PC-create Conversation DISPLAY_NAME positive testcases",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationDisplayNamePositiveTc(String displayName) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(displayName)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(TTL).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response
            .getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
  }

  @Test(description = "PC-create Conversation with ImageUrl Empty ")
  public void createConversationImageUrlEmpty() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(DISPLAY_NAME)
            .image_url("")
            .properties(PropertiesObj.builder().ttl(1).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response
            .getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();
  }

  @Test(description = "PC-create Conversation with TTL String num ")
  public void createConversationTTLStringNum() {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(DISPLAY_NAME)
            .image_url("")
            .properties(PropertiesObj.builder().ttl(Integer.parseInt("1")).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(
        response
            .getHref()
            .equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.getId()));
    softAssert.assertAll();
  }

  // can be a string, float allowed 1.00,1.34344
  // todo : property cases
  // other than key-value pair defined

}
