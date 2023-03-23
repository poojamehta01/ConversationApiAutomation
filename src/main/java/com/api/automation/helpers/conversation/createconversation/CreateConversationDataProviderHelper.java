package com.api.automation.helpers.conversation.createconversation;

import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static io.restassured.http.Method.DELETE;
import static io.restassured.http.Method.PATCH;
import static io.restassured.http.Method.PUT;

import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;

public class CreateConversationDataProviderHelper {

  @DataProvider(name = "conversationApi_dataProvider")
  public static Object[][] conversationApi_dataProvider(Method m) {
    int[] arr = {};
    switch (m.getName()) {
      case "createConversationNameValid":
        return new Object[][] {{""}, {"   "}, {"test_" + randomRegex("[a-z0-9]{101}")}};

      case "createConversationNameInValid":
        return new Object[][] {{null}, {true}, {1}, {11.22}, {arr}};

      case "createConversationDisplayNameValid":
        return new Object[][] {
          {null}, {"  "},
        };
      case "createConversationDisplayNameInvalid":
        return new Object[][] {{""}, {true}, {1}, {11.22}, {arr}};
      case "createConversationImageUrlInValid":
        return new Object[][] {{"  "}, {"test"}, {null}, {true}, {1}, {11.22}, {arr}};
      case "createConversationHttpMethodInValid":
        return new Object[][] {{DELETE}, {PUT}, {PATCH}};
      case "createConversationTTLValid":
        return new Object[][] {{1.2323}, {0}, {"1"}, {"1000000000000"}, {101.22}};
      case "createConversationTTLInvalid":
        return new Object[][] {{-1}, {"10000000000000000"}, {null}, {"  "}, {""}};

      default:
        return new Object[][] {};
    }
  }
}
