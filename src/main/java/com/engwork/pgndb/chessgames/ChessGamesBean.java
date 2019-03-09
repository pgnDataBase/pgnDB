package com.engwork.pgndb.chessgames;

import com.engwork.pgndb.chessgames.ChessGamesRequest.ChessGamesMetadataRequest;

public interface ChessGamesBean {

  ChessGamesMetadataResponse getChessGames(ChessGamesMetadataRequest chessGamesMetadataRequest);

  Integer countChessGames(String dataSource);

}
