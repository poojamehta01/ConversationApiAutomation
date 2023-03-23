package com.api.automation.conversation;

import static com.api.automation.constants.CommonConstants.APP_ID;
import static com.api.automation.constants.CommonConstants.PRIVATE_KEY;

import com.api.automation.pojos.requests.conversation.createconversation.CreateConversationRequest;
import com.api.automation.pojos.response.conversation.CreateConversationResponse;
import com.vonage.jwt.Jwt;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.nio.file.Paths;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Slf4j
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
@Epic("API Automation")
@Feature("Conversation Api")
@Story("List Conversation Positive and Negative testCases")
public class ListConversationTests {
  private String jwtToken;
  Map<String, Object> responseMap;
  private CreateConversationResponse createConversationResponse;
  private CreateConversationRequest createConversationRequest;

  @BeforeClass(alwaysRun = true)
  public void setup() {

    jwtToken =
        Jwt.builder()
            .applicationId(APP_ID)
            .privateKeyPath(Paths.get(PRIVATE_KEY))
            .build()
            .generate();
  }
}
