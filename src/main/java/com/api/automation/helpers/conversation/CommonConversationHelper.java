package com.api.automation.helpers.conversation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;

import com.api.automation.enums.conversation.CommonErrorEnums;
import com.api.automation.enums.conversation.InputValidationErrorEnums;
import com.api.automation.pojos.response.conversation.ErrorResponse;
import com.vonage.jwt.Jwt;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.testng.asserts.SoftAssert;

public class CommonConversationHelper {

  public static void assertInputValidationFail(
      ErrorResponse response, InputValidationErrorEnums enums) {
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(response.getDescription(), enums.getDescription());
    softAssert.assertEquals(response.getCode(), enums.getCode());
  }

  public static void assertErrorValidation(ErrorResponse response, CommonErrorEnums enums) {
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(response.getDescription(), enums.getDescription());
    softAssert.assertEquals(response.getCode(), enums.getCode());
  }

  public static String generateDate(int year, int month, int day) {
    LocalDateTime date = LocalDateTime.of(year, month, day, 0, 0, 0);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return date.format(formatter).replace(" ", "%20");
  }

  public static String generateJwtToken() {
    String jwtToken =
        Jwt.builder()
            .applicationId(APP_ID)
            .privateKeyPath(Paths.get(PRIVATE_KEY))
            .build()
            .generate();
    return jwtToken;
  }
}
