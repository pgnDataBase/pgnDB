package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import java.util.List;

public interface MetadataLoaderBean {
  void insertMany(List<ChessGameMetadataDAO> metadata, String databaseName);
}
