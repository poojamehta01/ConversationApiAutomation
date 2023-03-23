package com.api.automation.pojos.requests.conversation.createconversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateConversationRequest {

  public Object name;
  public Object display_name;
  public Object image_url;
  public PropertiesObj properties;

  @Data
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PropertiesObj {
    public Object ttl;
  }
}
