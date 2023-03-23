package com.api.automation.helpers;

import static com.api.automation.constants.CommonConstants.BASE_URL;
import static com.api.automation.constants.ConversationApiConstants.DISPLAY_NAME;
import static com.api.automation.constants.ConversationApiConstants.DUPLICATE_NAME;
import static com.api.automation.constants.ConversationApiConstants.IMAGE_URL;
import static com.api.automation.constants.ConversationApiConstants.TTL;
import static com.api.automation.constants.EndPointConstants.CONVERSATION_ENDPOINT;
import static com.api.automation.helpers.CommonTestHelper.getJsonString;
import static io.restassured.http.Method.POST;

import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest;
import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest.PropertiesObj;
import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequestIncorrectKey;

import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class ConversationApiHelper {

  public static Map<String, Object> getHeaders(String jwtToken) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + jwtToken);
    headers.put("Content-type", "application/json");
    return headers;
  }

  public static ApiRequestBuilder createConversationBuilder(
      Method methodType,
      int expectedStatusCode,
      CreateConversationRequest createConversationRequest,
      String jwtToken) {
    System.out.println(BASE_URL);
    return ApiRequestBuilder.builder()
        .requestMethod(methodType)
        .requestHeaders(getHeaders(jwtToken))
        .requestBody(getJsonString(createConversationRequest))
        .endPoint(BASE_URL + CONVERSATION_ENDPOINT)
        .expectedStatusCode(expectedStatusCode)
        .build();
  }

  public static ApiRequestBuilder createConversationBuilder(
      Method methodType,
      int expectedStatusCode,
      CreateConversationRequestIncorrectKey createConversationRequestIncorrectKey,
      String jwtToken) {
    System.out.println(BASE_URL);
    return ApiRequestBuilder.builder()
        .requestMethod(methodType)
        .requestHeaders(getHeaders(jwtToken))
        .requestBody(getJsonString(createConversationRequestIncorrectKey))
        .endPoint(BASE_URL + CONVERSATION_ENDPOINT)
        .expectedStatusCode(expectedStatusCode)
        .build();
  }


  public static ApiRequestBuilder createConversationBuilder(
      Method methodType, int expectedStatusCode, String jwtToken) {
    System.out.println(BASE_URL);
    return ApiRequestBuilder.builder()
        .requestMethod(methodType)
        .requestHeaders(getHeaders(jwtToken))
        .endPoint(BASE_URL + CONVERSATION_ENDPOINT)
        .expectedStatusCode(expectedStatusCode)
        .build();
  }

  public static void createConversation(String jwtToken) {
    PropertiesObj prop = PropertiesObj.builder().ttl(TTL).build();
    CreateConversationRequest createConversationRequest =
        CreateConversationRequest.builder()
            .display_name(DISPLAY_NAME)
            .name(DUPLICATE_NAME)
            .image_url(IMAGE_URL)
            .properties(prop)
            .build();
    ApiRequestBuilder createConversationBuilder =
        ConversationApiHelper.createConversationBuilder(
            POST, 200, createConversationRequest, jwtToken);
    Response createConversationResponse =
        RestAssuredUtils.processApiRequest(createConversationBuilder);
  }
}
