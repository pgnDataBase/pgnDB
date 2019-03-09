package com.engwork.pgndb.moveediting.moveparser.utils;

import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;

public class PieceUtils {

  private static boolean isWhitePiece(Integer piece){
    return piece < PieceColor.BLACK.value;
  }

  public static Integer getPieceNumber(Integer piece){
    if(isWhitePiece(piece))
      return piece - PieceColor.WHITE.value;
    else
      return piece - PieceColor.BLACK.value;
  }

  public static Integer getPieceColor(Integer piece){
    if(isWhitePiece(piece))
      return PieceColor.WHITE.value;
    else
      return PieceColor.BLACK.value;
  }

  public static String getPieceCodeString(Integer piece){
    Integer color = getPieceColor(piece);
    Integer pieceCode = getPieceNumber(piece);
    return PieceColor.getByValue(color).colorCode+ Piece.getByValue(pieceCode).getPieceCode();
  }
}
