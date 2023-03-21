package com.api.automation.secrets;

import static com.api.automation.constants.CommonConstants.SECRETS_FILE_NAME;

import com.api.automation.helpers.CommonTestHelper;

public class secrets {

  public static final String API_KEY =
      CommonTestHelper.getSecretValues("apiKey", SECRETS_FILE_NAME, "");
  public static final String API_SECRET =
      CommonTestHelper.getSecretValues("apiSecret", SECRETS_FILE_NAME, "");
  public static final String APP_ID =
      CommonTestHelper.getSecretValues("appId", SECRETS_FILE_NAME, "");

}
