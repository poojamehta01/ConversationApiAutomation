package com.api.automation.constants;

import org.json.simple.JSONObject;

public class CommonConstants {

  JSONObject jsonObject = new JSONObject();

  private CommonConstants() {
    throw new IllegalStateException("Object should not be created for CommonConstants");
  }

  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String GROUP_NAME = "groupName";
  public static final String MODULE_NAME = "moduleName";
  public static final String REPORT_NAME = "reportName";
  public static final String BASE_URL = "https://api.nexmo.com/v0.1";
  public static final String CONFIG_PROPERTIES = "config.properties";
  public static final String PRIVATE_KEY = "src/main/resources/private.key";
  public static final String APP_ID = "4962f474-1c07-41bf-bffb-13c324acd34e";

  public static final String[] NAME_ERROR =
      new String[] {"name:[\"\\\"name\\\" must be a string\"]"};

  public static final String[] DISPLAY_NAME_ERROR =
      new String[] {"name:[\"\\\"name\\\" must be a string\"]"};
}
