package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;

import java.util.List;

public interface ChessGamesLoaderBean {
  void loadChessGames(List<ChessGameData> chessGameDataList, String dataSource, Boolean statusMonitoring);
}
