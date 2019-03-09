package com.engwork.pgndb.chessgamesconverter.model;

import com.engwork.pgndb.pgnparser.pgn.Meta;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ChessGameMetadata {

  private UUID gameId;
  @JsonProperty("Event")
  private String event = "";
  @JsonProperty("Site")
  private String site = "";
  @JsonProperty("White")
  private String whitePlayer = "";
  @JsonProperty("Black")
  private String blackPlayer = "";
  @JsonProperty("Result")
  private String result = "";
  @JsonProperty("Date")
  private String date = "";
  @JsonProperty("Round")
  private String round = "";
  private Map<String, String> additional =new HashMap<>();

  // Seven Tag Roaster
  private static final String EVENT = "Event";
  private static final String SITE = "Site";
  private static final String DATE = "Date";
  private static final String ROUND = "Round";
  private static final String WHITE = "White";
  private static final String BLACK = "Black";
  private static final String RESULT = "Result";

  private void addAdditionalField(String key, String value) {
    this.additional.put(key, value);
  }

  public static ChessGameMetadata create(List<Meta> tagPairs) {
    ChessGameMetadata chessGameMetadata = new ChessGameMetadata();
    for (Meta meta : tagPairs) {
      String key = meta.getKey().trim();
      String value = meta.getValue();
      switch (key) {
        case EVENT:
          chessGameMetadata.setEvent(value);
          break;
        case SITE:
          chessGameMetadata.setSite(value);
          break;
        case DATE:
          chessGameMetadata.setDate(value);
          break;
        case ROUND:
          chessGameMetadata.setRound(value);
          break;
        case WHITE:
          chessGameMetadata.setWhitePlayer(value);
          break;
        case BLACK:
          chessGameMetadata.setBlackPlayer(value);
          break;
        case RESULT:
          chessGameMetadata.setResult(value);
          break;
        default:
          chessGameMetadata.addAdditionalField(key, value);
          break;
      }
    }
    return chessGameMetadata;
  }
}