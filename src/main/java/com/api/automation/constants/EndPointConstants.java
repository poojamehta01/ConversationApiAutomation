package com.api.automation.constants;

public class EndPointConstants {

  public static final String CONVERSATION_ENDPOINT = "/conversations";
  public static final String USER_ENPOINT = CONVERSATION_ENDPOINT+"/{CONVERSATION_ID}/users";
  public static final String MESSAGES_ENPOINT = CONVERSATION_ENDPOINT+ "/{CONVERSATION_ID}/messages";
  public static final String EVENTS_ENPOINT = "/events";
  public static final String LEGS_ENPOINT =CONVERSATION_ENDPOINT+ "/{CONVERSATION_ID}/legs";
  public static final String MEMBERS_ENPOINT = CONVERSATION_ENDPOINT+ "/{CONVERSATION_ID)/members";

}
