package com.api.automation.pojos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties
public class CreateConversationRequestTTLString {

  public String name;
  public String display_name;
  public String image_url;
  public PropertiesObj properties;

  @Data
  @Builder
  public static class PropertiesObj {
    public String ttl;
  }
}
