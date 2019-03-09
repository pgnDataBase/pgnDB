package com.engwork.pgndb.moveediting.moveparser.moveanalyzer;

import com.engwork.pgndb.chessgamesconverter.model.Piece;

public class MoveAnalyzerFactory {

  public static MoveAnalyzer getMoveAnalyzer(Integer pieceCodeNumber) {
    if(pieceCodeNumber.equals(Piece.PAWN.getValue()))
      return new PawnMoveAnalyzer();
    if(pieceCodeNumber.equals(Piece.KNIGHT.getValue()))
      return new KnightMoveAnalyzer();
    if(pieceCodeNumber.equals(Piece.KING.getValue()))
      return new KingMoveAnalyzer();
    return new  QBRMoveAnalyzer();
  }
}
