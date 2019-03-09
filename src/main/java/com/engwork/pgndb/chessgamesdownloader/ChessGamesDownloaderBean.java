package com.engwork.pgndb.chessgamesdownloader;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;

import java.util.UUID;

public interface ChessGamesDownloaderBean {
  ChessGameData getChessGameDataById(UUID id, String dataSource);
}
