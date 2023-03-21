package com.api.automation.helpers;

import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;

public class ConversationApiDataProviderHelper {

  @DataProvider(name = "conversationApi_dataProvider")
  public static Object[][] conversationApi_dataProvider(Method m) {

    switch (m.getName()) {
      case "createConversation":
        return new Object[][]{

        };
      default:
        return new Object[][] {};
    }
  }
}
