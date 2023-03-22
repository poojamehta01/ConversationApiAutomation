package com.api.automation.helpers;

import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static io.restassured.http.Method.DELETE;
import static io.restassured.http.Method.PATCH;
import static io.restassured.http.Method.PUT;

import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;

public class CreateConversationDataProviderHelper {

  @DataProvider(name = "conversationApi_dataProvider")
  public static Object[][] conversationApi_dataProvider(Method m) {

    switch (m.getName()) {
      case "createConversationNamePositiveTc":
        return new Object[][] {{""}, {"   "}, {"test_" + randomRegex("[a-z0-9]{1001}")}};

      case "createConversationDisplayNamePositiveTc":
        return new Object[][] {
          {null}, {"  "},
        };

      case "createConversationImageUrlIncorrect":
        return new Object[][] {{"  "}, {"test"}, {null}};
      case "createConversationIncorrectHttpMethod":
        return new Object[][] {{DELETE}, {PUT}, {PATCH}};

        //      case "createConversationPropertiesIncorrect":
        //
        //        return new Object[][]{
        //            {null},
        //            {{"incorrectKey":incorrectValue}},
        //        };

      default:
        return new Object[][] {};
    }
    // name
    // postiive
    // unique name only string, empty
    // negative
    // other than string, null
    // display name
    // P - duplicate allowed only string
    // N - other than string, null
    // url
    // P- string but uri
    // N- other than url, null
    // properties
    // P - map object, {} empty map
    // N - other than map object, null
    // TTL
    // P - only num
    // N - other than num, negative num

  }
}
