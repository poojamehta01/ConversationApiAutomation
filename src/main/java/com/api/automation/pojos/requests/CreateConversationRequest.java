package com.api.automation.pojos.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateConversationRequest {

  public String name;
  public String display_name;
  public String image_url;
  public PropertiesObj properties;

  @Data
  @Builder
  public static class PropertiesObj{
    public int ttl;
  }


}
