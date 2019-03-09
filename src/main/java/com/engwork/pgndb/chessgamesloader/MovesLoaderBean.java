package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.Move;

import java.util.List;

public interface MovesLoaderBean {
  void insertMany(List<Move> moves, String databaseName);
}
