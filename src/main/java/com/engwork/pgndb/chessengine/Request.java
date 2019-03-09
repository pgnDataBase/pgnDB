package com.engwork.pgndb.chessengine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

class Request {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class EngineStartRequest {
    private String engine;
  }
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class StartPositionRequest{
    private String engine;
    private String fen;
    private Integer seconds;
  }
  @Data
  @AllArgsConstructor
  static class LoginRequest {
    private String login;
    private String password;
  }
}
