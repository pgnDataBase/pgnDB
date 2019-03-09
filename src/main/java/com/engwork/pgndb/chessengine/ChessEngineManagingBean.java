package com.engwork.pgndb.chessengine;

interface ChessEngineManagingBean {
  Response.EnginesResponse getEngines();
  void startEngine(String engine,String position, Integer time);
  Response.EngineStopResponse stopEngine();
}
