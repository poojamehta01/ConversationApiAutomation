package com.api.automation.helpers.conversation;

import static com.api.automation.constants.CommonConstants.BASE_URL;
import static com.api.automation.constants.EndPointConstants.CONVERSATION_ENDPOINT;
import static com.api.automation.helpers.CommonTestHelper.getJsonString;

import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest;
import com.api.automation.utils.ApiRequestBuilder;
import io.restassured.http.Method;
import java.util.HashMap;
import java.util.Map;

public class UpdateConversationApiHelper {
  public static Map<String, Object> getHeaders(String jwtToken) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + jwtToken);
    headers.put("Content-type", "application/json");
    return headers;
  }

  public static ApiRequestBuilder updateConversationBuilder(
      Method methodType,
      int expectedStatusCode,
      String jwtToken,
      Object conversationId,
      CreateConversationRequest createConversationRequest) {
    return ApiRequestBuilder.builder()
        .requestMethod(methodType)
        .requestHeaders(getHeaders(jwtToken))
        .requestBody(getJsonString(createConversationRequest))
        .endPoint(BASE_URL + CONVERSATION_ENDPOINT + "/" + conversationId)
        .expectedStatusCode(expectedStatusCode)
        .build();
  }
}
