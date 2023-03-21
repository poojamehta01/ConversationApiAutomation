package com.api.automation;

import static com.api.automation.helpers.ConversationApiHelper.getHeaders;
import static io.restassured.http.Method.POST;

import com.api.automation.helpers.ConversationApiHelper;
import com.api.automation.helpers.JWTTokenGenerator;
import com.api.automation.pojos.requests.CreateConversationRequeset;
import com.api.automation.pojos.response.CreateConversationResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import io.restassured.response.Response;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import org.junit.Before;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ConversationApiTests extends BaseTest  {

  private String jwtToken;

  @BeforeClass(alwaysRun = true)
  public void setup() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
    jwtToken = JWTTokenGenerator.generateJwtToken();
    System.out.println("this is jwtToken \n"+jwtToken);
  }
  @Test(description = "create Conversation")

  public void createConversation()
        {

    CreateConversationRequeset createConversationRequest=null;
    ApiRequestBuilder createConversationBuilder = ConversationApiHelper.createConversation(POST,201, createConversationRequest,jwtToken);
    System.out.println(createConversationBuilder);
    Response createConversationResponse = RestAssuredUtils.processApiRequest(createConversationBuilder);
    CreateConversationResponse response = createConversationResponse.as(CreateConversationResponse.class);
    System.out.println(response);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertNotNull(response);
  }
}
