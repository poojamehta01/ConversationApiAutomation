package com.api.automation.enums.conversation;

import lombok.Getter;

public enum InputValidationErrorEnums {
  DATATYPE_OTHER_THAN_STRING(
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

  DATATYPE_OTHER_THAN_INT(
      "Input validation failure.", "\"$KEYNAME\" must be a number", "http:error:validation-fail"),

  TTL_NOT_SAFE_NUM(
      "Input validation failure.", "\"ttl\" must be a safe number", "http:error:validation-fail"),

  GREATER_THAN_EQUAL_TO_ZERO(
      "Input validation failure.",
      "\"$KEYNAME\" must be greater than or equal to 0",
      "http:error:validation-fail"),
  INCORRECT_KEY(
      "Input validation failure.", "\"$KEYNAME\" is not allowed", "http:error:validation-fail"),

  EMPTY_CONVERSATION_ID(
      "Input validation failure.",
      "\"conversation_id\" is not allowed to be empty",
      "http:error:validation-fail"),

  DATE_FORMAT(
      "Input validation failure.",
      "\"$KEYNAME\" must be in ISO 8601 date format",
      "http:error:validation-fail"),

  LESS_THAN_EQUAL_TO_1(
      "Input validation failure.",
      "\"$KEYNAME\" must be greater than or equal to 1",
      "http:error:validation-fail"),
  LESS_THAN_EQUAL_TO_100(
      "Input validation failure.",
      "\"$KEYNAME\" must be less than or equal to 100",
      "http:error:validation-fail");

  @Getter private final String description;
  @Getter private final String error;
  @Getter private final String code;

  InputValidationErrorEnums(String description, String error, String code) {
    this.description = description;
    this.error = error;
    this.code = code;
  }
}
