package com.engwork.pgndb.chessengine.wsclient;

public interface WebSocketClientBean {
  void startEvaluatingPosition(String url,String token,String position, Integer time);
}
