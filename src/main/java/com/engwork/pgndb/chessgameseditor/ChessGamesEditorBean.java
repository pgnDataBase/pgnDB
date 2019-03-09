package com.engwork.pgndb.chessgameseditor;


import com.engwork.pgndb.chessgames.DeleteGamesRequest;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;

public interface ChessGamesEditorBean {
  void updateChessGameMetadata(String databaseName, ChessGameMetadata chessGameMetadata);
  void deleteGames(DeleteGamesRequest deleteGamesRequest);
}
