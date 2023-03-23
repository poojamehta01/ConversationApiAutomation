package com.api.automation.pojos.response.conversation;

import java.util.ArrayList;
import lombok.Data;

@Data
public class ListConversationResponse {

  public int count;
  public int page_size;
  public int record_index;
  public Embedded _embedded;
  public Links _links;

  @Data
  public static class Conversation {

    public String uuid;
    public String name;
    public Links _links;
  }

  @Data
  public static class Embedded {

    public ArrayList<Conversation> conversations;
  }

  @Data
  public static class Links {

    public Self self;
  }

  @Data
  public static class Self {

    public String href;
  }
}
