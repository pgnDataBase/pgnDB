package com.engwork.pgndb.chessengine.engineinfo;

import java.util.List;

public interface EngineInfoManagingBean {
  void insertEngineInfo(EngineInfo engineInfo);
  void deleteEngineInfo();
  List<EngineInfo> getEngineInfo();
}
