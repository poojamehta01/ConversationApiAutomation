package com.api.automation.helpers;

import static com.api.automation.constants.CommonConstants.BASE_URL;
import static com.api.automation.constants.ConversationApiConstants.CONVERSATION_ENDPOINT;
import static com.api.automation.helpers.CommonTestHelper.getJsonString;

import com.api.automation.pojos.requests.CreateConversationRequeset;
import com.api.automation.utils.ApiRequestBuilder;
import io.restassured.http.Method;
import java.util.HashMap;
import java.util.Map;

public class ConversationApiHelper {


  public static Map<String, Object> getHeaders(String jwtToken) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + jwtToken);
    headers.put("Content-type", "application/json");
    return headers;
  }
  public static ApiRequestBuilder createConversation(Method methodType,int expectedStatusCode, CreateConversationRequeset createConversationRequeset) {

    return ApiRequestBuilder.builder()
        .requestMethod(methodType)
        .
        .requestBody(getJsonString(createConversationRequeset))
        .endPoint(BASE_URL+CONVERSATION_ENDPOINT)
        .expectedStatusCode(expectedStatusCode)
        .build();
  }



}
