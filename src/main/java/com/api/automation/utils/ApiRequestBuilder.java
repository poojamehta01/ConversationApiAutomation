package com.api.automation.utils;

import io.restassured.http.Method;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Builds any API request that can be sent to process
 *
 */
@Data
@Builder
public class ApiRequestBuilder {

  @NonNull private String endPoint;

  @NonNull private Method requestMethod;

  private Map<String, Object> requestHeaders;

  private String requestBody;

  private Object requestBodyObj;

  private Map<String, Object> queryParams;

  private Map<String, Object> formParams;

  private boolean isMultipart;

  private Map<String, Object> multipart;

  private Boolean allowRedirects;

  @Builder.Default private Boolean enableLogging = true;

  @Builder.Default private Boolean urlEncodingEnabled = false;

  private int expectedStatusCode;
}
