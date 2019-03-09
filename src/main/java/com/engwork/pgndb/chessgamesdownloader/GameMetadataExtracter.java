package com.engwork.pgndb.chessgamesdownloader;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;

public interface GameMetadataExtracter {
  ChessGameMetadata extract(String databaseName, ChessGameMetadataDAO chessGameMetadataDAO);
}
