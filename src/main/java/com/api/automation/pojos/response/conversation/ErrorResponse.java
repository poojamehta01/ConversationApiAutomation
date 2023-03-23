package com.api.automation.pojos.response.conversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@JsonIgnoreProperties
public class ErrorResponse {

  @Data
  @Getter
  @JsonIgnoreProperties
  public class Error {

    public ArrayList<String> name;
    public ArrayList<String> display_name;
    public ArrayList<String> image_url;
    public ArrayList<String> properties;
    public ArrayList<String> ttl;
    public ArrayList<String> incorrectKey;
    public ArrayList<String> conversation_id;
    public ArrayList<String> page_size;
    public ArrayList<String> date_start;
    public ArrayList<String> date_end;
    public ArrayList<String> record_index;
    public ArrayList<String> order;
  }

  public String description;
  public Error error;
  public String code;
}
