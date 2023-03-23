package com.api.automation.enums.conversation;

import lombok.Getter;

public enum CommonErrorEnums {
  DUPLICATE_NAME_ERROR(
      "The request failed because the conversation name already exists. Please provide a unique conversation name and try again.",
      "",
      "conversation:error:duplicate-name"),

  TOKEN_EXPIRED_ERROR(
      "You provided an expired token. Please provide a valid token.",
      "",
      "system:error:expired-token"),

  TOKEN_INVALID_ERROR(
      "You did not provide a valid token. Please provide a valid token.",
      "",
      "system:error:invalid-token"),
  INCORRECT_HTTP_METHODS("Request method not supported.", "", "http:error:method-not-allowed"),

  INVALID_CONVERSATION_ID(
      "Conversation does not exist, or you do not have access.",
      "",
      "conversation:error:not-found");

  @Getter private final String description;
  @Getter private final String error;
  @Getter private final String code;

  CommonErrorEnums(String description, String error, String code) {
    this.description = description;
    this.error = error;
    this.code = code;
  }
}
