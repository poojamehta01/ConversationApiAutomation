package com.api.automation;

import static com.api.automation.constants.CommonConstants.BASE_URL;
import static com.api.automation.constants.CommonConstants.CONFIG_PROPERTIES;

import com.api.automation.helpers.CommonTestHelper;
import com.api.automation.listners.TestListener;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Slf4j
@Listeners(TestListener.class)
public class BaseTest {

  public String baseUrl;
  public CommonTestHelper commonTestHelper = new CommonTestHelper();

  @BeforeClass(alwaysRun = true)
  @Parameters({BASE_URL})
  public void startTest(@Optional String testNgBaseUrl) {
    this.baseUrl =
        testNgBaseUrl == null
            ? CommonTestHelper.getConfigParameter(null, BASE_URL, CONFIG_PROPERTIES)
            : testNgBaseUrl;
  }
}
