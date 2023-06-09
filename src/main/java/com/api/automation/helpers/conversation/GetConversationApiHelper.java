package com.api.automation.helpers.conversation;

import static com.api.automation.constants.CommonConstants.BASE_URL;
import static com.api.automation.constants.EndPointConstants.CONVERSATION_ENDPOINT;
import static io.restassured.http.Method.GET;

import com.api.automation.pojos.IncorrectBodyRequest;
import com.api.automation.utils.ApiRequestBuilder;
import io.restassured.http.Method;
import java.util.HashMap;
import java.util.Map;

public class GetConversationApiHelper {

  public static Map<String, Object> getHeaders(String jwtToken) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + jwtToken);
    headers.put("Content-type", "application/json");
    return headers;
  }

  public static ApiRequestBuilder getConversationBuilder(
      Method methodType, int expectedStatusCode, String jwtToken, Object conversationId) {
    return ApiRequestBuilder.builder()
        .requestMethod(methodType)
        .requestHeaders(getHeaders(jwtToken))
        .endPoint(BASE_URL + CONVERSATION_ENDPOINT + "/" + conversationId)
        .expectedStatusCode(expectedStatusCode)
        .build();
  }

  public static ApiRequestBuilder getConversationBuilder(
      Method methodType,
      String jwtToken,
      Object conversationId,
      IncorrectBodyRequest incorrectBodyRequest) {
    return ApiRequestBuilder.builder()
        .requestMethod(GET)
        .requestHeaders(getHeaders(jwtToken))
        .endPoint(BASE_URL + CONVERSATION_ENDPOINT + "/" + conversationId)
        .requestBody(String.valueOf(incorrectBodyRequest))
        .expectedStatusCode(200)
        .build();
  }
}
