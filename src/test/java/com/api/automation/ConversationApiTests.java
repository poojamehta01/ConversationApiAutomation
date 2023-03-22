package com.api.automation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;
import static com.api.automation.constants.ConversationApiConstants.DISPLAY_NAME;
import static com.api.automation.constants.ConversationApiConstants.IMAGE_URL;
import static com.api.automation.constants.ConversationApiConstants.NAME;
import static com.api.automation.constants.ConversationApiConstants.TTL;
import static io.restassured.http.Method.POST;

import com.api.automation.helpers.ConversationApiHelper;
import com.api.automation.pojos.requests.CreateConversationRequest;
import com.api.automation.pojos.requests.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.response.CreateConversationResponse;
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
    System.out.println("this is jwtToken \n" + jwtToken);
  }

  @Test(description = "create Conversation")

  public void createConversation() {

    System.out.println("this is token in test");
    PropertiesObj prop = PropertiesObj.builder().ttl(TTL).build();
    CreateConversationRequest createConversationRequest = CreateConversationRequest.builder()
        .display_name(DISPLAY_NAME)
        .name(NAME)
        .image_url(IMAGE_URL)
        .properties(prop)
        .build();
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversation(POST,
        200, createConversationRequest, jwtToken);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(
        createConversationBuilder);
    CreateConversationResponse response = createConversationResponse.as(
        CreateConversationResponse.class);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response.id);
    softAssert.assertNotNull(response.href);
    softAssert.assertTrue(response.id.contains("CON-"));
    softAssert.assertTrue(
        response.href.equals("https://api-us-3.vonage.com/v0.1/conversations/" + response.id));
  }
}
