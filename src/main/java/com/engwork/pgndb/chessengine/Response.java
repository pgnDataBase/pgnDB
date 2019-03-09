package com.engwork.pgndb.chessengine;

import java.util.List;
import lombok.Data;

class Response {
  @Data
  static class LoginResponse {
    private Boolean success;
    private String token;
  }

  @Data
  static class EngineStopResponse {
    private Boolean success;
    private String info;
  }

  @Data
  static class EnginesResponse {
    private Boolean success;
    private List<ChessEngine> info;
  }
}
