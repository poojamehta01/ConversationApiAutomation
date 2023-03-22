package com.api.automation.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/** Fetches data from a Properties File which is present in resources folder. */
@Slf4j
public class PropertiesUtil {

  private PropertiesUtil() {
    throw new IllegalStateException("Object should not be created for PropertiesUtil");
  }

  private static final String PROPERTIES_EXTENSION = ".properties";
  private static final String SLASH = "/";

  public static Properties getFileProperties(String fileName) {
    fileName = fileName.startsWith(SLASH) ? fileName : SLASH + fileName;
    fileName = fileName.endsWith(PROPERTIES_EXTENSION) ? fileName : fileName + PROPERTIES_EXTENSION;

    Properties properties = new Properties();
    try (InputStream inputStream = PropertiesUtil.class.getResourceAsStream(fileName)) {
      properties.load(inputStream);
      log.info("Properties File: " + fileName + " Loaded!");
    } catch (FileNotFoundException e) {
      log.error("File not found in this environment!");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }
}
