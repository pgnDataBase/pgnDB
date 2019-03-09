package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.Position;

import java.util.Map;

public interface PositionsLoaderBean {
  Map<String, Position> insertWithIdCompetition(Map<String, Position> players, String databaseName);
}
