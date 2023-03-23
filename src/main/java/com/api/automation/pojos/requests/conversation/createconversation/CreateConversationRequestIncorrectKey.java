package com.api.automation.pojos.requests.conversation.createconversation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateConversationRequestIncorrectKey {

  public String name;
  public String display_name;
  public String image_url;
  public PropertiesObjIncorrect properties;

  @Data
  @Builder
  public static class PropertiesObjIncorrect {
    public boolean ttl;
    public int incorrectKey;
  }
}
