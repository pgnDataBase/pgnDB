package com.engwork.pgndb.moveediting;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;

import lombok.Data;

@Data
class SaveChangesRequest {
  private ChessGameData chessGameData;
  private String databaseName;
}
