package com.api.automation.secrets;

import static com.api.automation.constants.CommonConstants.SECRETS_FILE_NAME;

import com.api.automation.helpers.CommonTestHelper;

public class secrets {

  public static final String CLIENT_ID =
      CommonTestHelper.getSecretValues("acClientId", SECRETS_FILE_NAME, "");
  public static final String CLIENT_SECRET =
      CommonTestHelper.getSecretValues("acClientSecret", SECRETS_FILE_NAME, "");

}
