package com.engwork.pgndb.chessgames;

import com.engwork.pgndb.searching.requestmodel.GamesSearchingFilter;
import lombok.Data;

public class ChessGamesRequest {
  @Data
  public static class ChessGamesMetadataRequest {
    private String databaseName;
    private Integer pageSize;
    private Integer offset;
    private GamesSearchingFilter filter;
  }

  @Data
  static class ChessGameRequest {
    private String databaseName;
  }

}

