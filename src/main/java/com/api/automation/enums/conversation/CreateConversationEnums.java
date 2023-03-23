package com.api.automation.enums.conversation;

import lombok.Getter;

public enum CreateConversationEnums {
  DUPLICATE_NAME_ERROR(
      "The request failed because the conversation name already exists. Please provide a unique conversation name and try again.",
      "",
      "conversation:error:duplicate-name"),

  DATATYPE_INCORRECT(
      "Input validation failure.", "\"$KEYNAME\" must be a string", "http:error:validation-fail"),
  EMPTY_DISPLAY_NAME(
      "Input validation failure.",
      "\"display_name\" is not allowed to be empty",
      "http:error:validation-fail"),

  INCORRECT_URL(
      "Input validation failure.",
      "\"image_url\" must be a valid uri",
      "http:error:validation-fail"),

  PROPERTY_DATATYPE_INCORRECT(
      "Input validation failure.",
      "\"properties\" must be of type object",
      "http:error:validation-fail"),

  TTL_DATATYPE_INCORRECT(
      "Input validation failure.", "\"ttl\" must be a number", "http:error:validation-fail"),

  TTL_NOT_SAFE_NUM(
      "Input validation failure.", "\"ttl\" must be a safe number", "http:error:validation-fail"),

  TTL_LESS_THAN_ZERO(
      "Input validation failure.",
      "\"ttl\" must be greater than or equal to 0",
      "http:error:validation-fail"),
  INCORRECT_KEY(
      "Input validation failure.", "\"$KEYNAME\" is not allowed", "http:error:validation-fail"),

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
      "conversation:error:not-found"),

  EMPTY_CONVERSATION_ID(
      "Input validation failure.",
          "\"conversation_id\" is not allowed to be empty",
          "http:error:validation-fail");

  @Getter private final String description;
  @Getter private final String error;
  @Getter private final String code;

  CreateConversationEnums(String description, String error, String code) {
    this.description = description;
    this.error = error;
    this.code = code;
  }
}
