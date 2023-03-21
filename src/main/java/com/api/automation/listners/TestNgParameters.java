package com.api.automation.listners;

import java.util.Map;
import org.testng.ISuite;

/**
 * Class to maintain testNg Parameters of a given testNg file
 *
 */
public class TestNgParameters {

  private Map<String, String> testNgParams;

  /**
   * Sets TestNg Suite params
   *
   * @param suite
   */
  public void setSuiteParams(ISuite suite) {
    testNgParams = suite.getXmlSuite().getAllParameters();
  }

  /**
   * Sets TestNg params based on key, value pair
   *
   * @param key
   * @param value
   */
  public void setSuiteParams(String key, String value) {
    testNgParams.put(key, value);
  }

  /**
   * Getter for TestNg params
   *
   * @param key
   * @return
   */
  public String getTestNGParamValue(String key) {
    return testNgParams.get(key);
  }

  /**
   * Checks if a testNg parameter is present or not
   *
   * @param key
   * @return
   */
  public boolean isTestNgParamPresent(String key) {
    return testNgParams.containsKey(key);
  }
}
