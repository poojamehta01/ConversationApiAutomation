package com.api.automation.helpers;

import static com.api.automation.constants.CommonConstants.BASE_URL;
import static com.api.automation.helpers.CommonTestHelper.getJsonString;
import static jdk.internal.net.http.websocket.OpeningHandshake.HEADER_ACCEPT;

import com.api.automation.pojos.requests.CreateConversationRequeset;
import com.api.automation.utils.ApiRequestBuilder;
import io.restassured.http.Method;
import java.util.HashMap;
import java.util.Map;

public class ConversationApiHelper {

  private static final String HEADER_TOKEN = null;

  private static Map<String, Object> getHeaders() {
    Map<String, Object> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + HEADER_TOKEN);
    headers.put("Content-type", "application/json");
    return headers;
  }
  public static ApiRequestBuilder createConversation(Method methodType, CreateConversationRequeset createConversationRequeset ) {

    return ApiRequestBuilder.builder()
        .requestMethod(methodType)
        .requestHeaders(getHeaders())
        .requestBody(getJsonString(createConversationRequeset))
        .endPoint(BASE_URL)
        .build();
  }



}
