package com.api.automation.helpers;

import static com.api.automation.helpers.CommonTestHelper.randomName;
import static com.api.automation.helpers.CommonTestHelper.randomRegex;
import static com.api.automation.helpers.CommonTestHelper.randomUri;

import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;

public class ConversationApiDataProviderHelper {

  @DataProvider(name = "conversationApi_dataProvider")
  public static Object[][] conversationApi_dataProvider(Method m) {

    switch (m.getName()) {
      case "createConversationNamePositive":
        return new Object[][]{
            // name,displayName, imageUrl, properties-ttl
            {"test_"+randomRegex("\"[a-z0-9]{251}\""),randomName(),randomUri(),"{ttl:1}"},

        };
      default:
        return new Object[][] {};
    }
    // name
    //postiive
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
