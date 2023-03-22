package com.api.automation.pojos.response;

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
  }

  public String description;
  public Error error;
  public String code;
}
