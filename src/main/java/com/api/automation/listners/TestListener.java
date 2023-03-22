package com.api.automation.listners;

import com.aventstack.extentreports.ExtentTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.ISuiteListener;
import org.testng.ITestListener;

/** Test Listener: Contains configurations to handle test case start, end, success, failure, skip */
@Slf4j
public class TestListener implements ITestListener, ISuiteListener {

  private ExtentTest test;
  private String groupName;
  private String moduleName;
  private String reportName;

  private static final String LINE = new String(new char[150]).replace('\0', '-');
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String SECRETS_FILE_NAME = "secrets.properties";
  private static final String REPORT_BUCKET = "automation-service-gamma";
  private static final String REPORT_PATH = "/tmp";
  private static final String GROUP_NAME = "groupName";
  private static final String MODULE_NAME = "moduleName";
  private static final String REPORT_NAME = "reportName";
  private static final String CONFIG_PROPERTIES = "config.properties";
}
