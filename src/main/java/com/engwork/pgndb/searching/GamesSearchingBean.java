package com.engwork.pgndb.searching;

import com.engwork.pgndb.chessgames.ChessGamesMetadataResponse;
import com.engwork.pgndb.chessgames.ChessGamesRequest.ChessGamesMetadataRequest;

public interface GamesSearchingBean {
  ChessGamesMetadataResponse search(ChessGamesMetadataRequest chessGamesMetadataRequest);
}
