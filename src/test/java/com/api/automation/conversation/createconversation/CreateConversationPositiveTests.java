package com.api.automation.conversation.createconversation;

import static com.api.automation.constants.conversation.CreateConversationConstants.DISPLAY_NAME;
import static com.api.automation.constants.conversation.CreateConversationConstants.HREF;
import static com.api.automation.constants.conversation.CreateConversationConstants.IMAGE_URL;
import static com.api.automation.constants.conversation.CreateConversationConstants.TTL;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static io.restassured.http.Method.POST;

import com.api.automation.BaseTest;
import com.api.automation.helpers.conversation.CommonConversationHelper;
import com.api.automation.helpers.conversation.createconversation.CreateConversationApiHelper;
import com.api.automation.helpers.conversation.createconversation.CreateConversationDataProviderHelper;
import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest;
import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.response.conversation.CreateConversationResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners({io.qameta.allure.testng.AllureTestNg.class})
@Epic("API Automation")
@Feature("Conversation Api")
@Story("Create Conversation Positive testCases")
public class CreateConversationPositiveTests extends BaseTest {

  private String jwtToken;

  @BeforeClass(alwaysRun = true)
  public void setup() {
    jwtToken = CommonConversationHelper.generateJwtToken();
  }

  @Test(description = "PC-create Conversation with name,display,imageUrl,properties")
  public void createConversation() {

    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[A-Za-z0-9]{25}"))
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(TTL).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        CreateConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(response.getHref().equals(HREF + response.getId()));
    softAssert.assertAll();
  }

  @Test(description = "PC-create Conversation with emptyBody")
  public void createConversationWithEmptyPayload() {

    ApiRequestBuilder createConversationBuilder =
        CreateConversationApiHelper.createConversationBuilder(POST, 200, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(response.getHref().equals(HREF + response.getId()));
    softAssert.assertAll();
  }

  @Test(
      description =
          "PC-create Conversation with all names emptyString,threeSpacesString,longName positive testcases",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationNameValid(String name) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name(name)
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(TTL).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        CreateConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(response.getHref().equals(HREF + response.getId()));
    softAssert.assertAll();
  }

  @Test(
      description = "PC-create Conversation DISPLAY_NAME null, with whiteSpace",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationDisplayNameValid(String displayName) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(displayName)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(TTL).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        CreateConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(response.getHref().equals(HREF + response.getId()));
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
        CreateConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(response.getHref().equals(HREF + response.getId()));
    softAssert.assertAll();
  }

  @Test(
      description = "PC-create Conversation with TTL 0,Float,String ",
      dataProvider = "conversationApi_dataProvider",
      dataProviderClass = CreateConversationDataProviderHelper.class)
  public void createConversationTTLValid(Object ttl) {
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .name("test_" + randomRegex("[a-z0-9]{21}"))
            .display_name(DISPLAY_NAME)
            .image_url(IMAGE_URL)
            .properties(PropertiesObj.builder().ttl(ttl).build())
            .build();
    ApiRequestBuilder createConversationBuilder =
        CreateConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response =
        createConversationResponse.as(CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.getId());
    softAssert.assertNotNull(response.getHref());
    softAssert.assertTrue(response.getId().contains("CON-"));
    softAssert.assertTrue(response.getHref().equals(HREF + response.getId()));
    softAssert.assertAll();
  }
}
