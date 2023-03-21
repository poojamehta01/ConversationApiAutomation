package com.api.automation.helpers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Commons Test Helper Methods
 *
 */
@Slf4j
public class CommonTestHelper {

  public static String getJsonString(String filePath)
      throws IOException, org.json.simple.parser.ParseException {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;
    InputStream inputStream = CommonTestHelper.class.getResourceAsStream(filePath);
    assert inputStream != null;
    jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream));
    log.info(jsonObject.toJSONString());
    return jsonObject.toJSONString();
  }

  public static String getJsonString(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = null;
    try {
      jsonString = mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return jsonString;
  }


}
