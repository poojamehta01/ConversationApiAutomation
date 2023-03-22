package com.api.automation.constants;

import static com.api.automation.helpers.CommonTestHelper.randomName;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static com.api.automation.helpers.CommonTestHelper.randomUri;

public class ConversationApiConstants {

  public static final String NAME = "test_" + randomRegex("[a-z0-9]{21}");
  public static final String DISPLAY_NAME = "test_" + randomName();
  public static final String IMAGE_URL = "https://" + randomUri();
  public static final int TTL = 1;


}
