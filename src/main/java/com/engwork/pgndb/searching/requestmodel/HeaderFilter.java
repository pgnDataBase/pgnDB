package com.engwork.pgndb.searching.requestmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class HeaderFilter {
  @JsonProperty("Event")
  private String event;
  @JsonProperty("Site")
  private String site;
  @JsonProperty("White")
  private String whitePlayer;
  @JsonProperty("Black")
  private String blackPlayer;
  @JsonProperty("Result")
  private String result;
  @JsonProperty("Date")
  private String date;
  @JsonProperty("Round")
  private String round;
  private Map<String, String> additional;
}
