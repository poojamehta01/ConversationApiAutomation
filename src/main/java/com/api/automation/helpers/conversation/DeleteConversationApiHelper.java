package com.api.automation.helpers.conversation;

import static com.api.automation.constants.CommonConstants.BASE_URL;
import static com.api.automation.constants.EndPointConstants.CONVERSATION_ENDPOINT;

import com.api.automation.utils.ApiRequestBuilder;
import io.restassured.http.Method;
import java.util.HashMap;
import java.util.Map;

public class DeleteConversationApiHelper{
    public static Map<String, Object> getHeaders(String jwtToken) {
      Map<String, Object> headers = new HashMap<>();
      headers.put("Authorization", "Bearer " + jwtToken);
      headers.put("Content-type", "application/json");
      return headers;
    }

    public static ApiRequestBuilder deleteConversationBuilder(
        Method methodType, int expectedStatusCode, String jwtToken, Object conversationId) {
      System.out.println(BASE_URL);
      return ApiRequestBuilder.builder()
          .requestMethod(methodType)
          .requestHeaders(getHeaders(jwtToken))
          .endPoint(BASE_URL + CONVERSATION_ENDPOINT + "/" + conversationId)
          .expectedStatusCode(expectedStatusCode)
          .build();
    }
}
