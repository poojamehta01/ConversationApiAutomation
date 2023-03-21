package com.api.automation;

import static io.restassured.http.Method.POST;

import com.api.automation.helpers.ConversationApiHelper;
import com.api.automation.pojos.requests.CreateConversationRequeset;
import com.api.automation.pojos.response.CreateConversationResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ConversationApiTests extends BaseTest  {

  @Test(description = "create Conversation")

  public void createConversation() {
    CreateConversationRequeset createConversationRequest=null;
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversation(POST,201, createConversationRequest);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response = createConversationResponse.as(CreateConversationResponse.class);
    System.out.println(response);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response);
  }
}
