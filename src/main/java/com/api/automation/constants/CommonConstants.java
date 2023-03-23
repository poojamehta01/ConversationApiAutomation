package com.api.automation.constants;

import org.json.simple.JSONObject;

public class CommonConstants {

  JSONObject jsonObject = new JSONObject();

  private CommonConstants() {
    throw new IllegalStateException("Object should not be created for CommonConstants");
  }

  public static final String BASE_URL = "https://api.nexmo.com/v0.1";
  public static final String CONFIG_PROPERTIES = "config.properties";
  public static final String PRIVATE_KEY = "src/main/resources/private.key";
  public static final String APP_ID = "4962f474-1c07-41bf-bffb-13c324acd34e";

  public static final String INVALID_TOKEN = "";
  public static final String EXPIRED_TOKEN =
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2Nzk0Njc3NTgsImV4cCI6MTY3OTQ4OTM1OCwianRpIjoiQjJ3Zzc5cUFJQ2pBIiwiYXBwbGljYXRpb25faWQiOiI0OTYyZjQ3NC0xYzA3LTQxYmYtYmZmYi0xM2MzMjRhY2QzNGUiLCJzdWIiOiIiLCJhY2wiOiIifQ.TLNOFOYbM-9BcMV0D94Lu5OYw7hOKVcQbdHJppXvq7HTjsquOeHmcStctp1F-4byfTw2L6phkG_boXtatrVUR8W6YJqlHhoJ1bqOuWtCpa-ZiGhaez3j5MaKJ1qwfDSvH4N8QA3thF7OSEvhGzPcT_jthQHx5kY-_PP3jtkMavG6bmFlNMmEkrN5_47O2YxhO2FsAORn6kY1IDtPS6Lzxoqg9-OEc6UFFcdl5cexkmDNKGwJ-Cl69Vn4LXwVIvIucth9bAwgNG2U3ubVq6y-2oCKu10EbbwpbheG2sqqJAsHb9aLq_GxQTQkHBLaauxiWjD8bDhRd0TMh07GSLlexw";
}
