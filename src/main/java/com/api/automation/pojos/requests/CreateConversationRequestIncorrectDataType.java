package com.api.automation.pojos.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateConversationRequestIncorrectDataType {

  public int name;
  public int display_name;
  public int image_url;
  public PropertiesObjInc properties;

  @Data
  @Builder
  public static class PropertiesObjInc {
    public boolean ttl;
    public int incorrectKey;
  }
}
