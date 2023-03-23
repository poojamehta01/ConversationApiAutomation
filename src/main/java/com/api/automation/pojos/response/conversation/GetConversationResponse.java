package com.api.automation.pojos.response.conversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetConversationApiResponse {

  public String uuid;
  public String name;
  public String display_name;
  public String image_url;
  public Timestamp timestamp;
  public int sequence_number;
  public ArrayList<Object> numbers;
  public String state;
  public Properties properties;
  public ArrayList<Object> members;
  public Links _links;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class CustomData {}

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Links {
    public Self self;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Properties {
    public int ttl;
    public CustomData custom_data;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Self {
    public String href;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Timestamp {
    public Date created;
  }
}
