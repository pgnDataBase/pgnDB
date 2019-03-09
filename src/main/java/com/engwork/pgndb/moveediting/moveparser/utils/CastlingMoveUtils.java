package com.engwork.pgndb.moveediting.moveparser.utils;

import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;

public class CastlingMoveUtils {

  public static boolean isValidCastlingMove(StringBuilder castlingInfo,String moveType, Integer pieceColor){
    if(castlingInfo!=null) {
      if(moveType.equals(MoveType.KSCG.name())){
        if(pieceColor.equals(PieceColor.WHITE.value) && castlingInfo.toString().contains("K") )
          return true;
        if(pieceColor.equals(PieceColor.BLACK.value) && castlingInfo.toString().contains("k") )
          return true;
      }
      if(moveType.equals(MoveType.QSCG.name())){
        if(pieceColor.equals(PieceColor.WHITE.value) && castlingInfo.toString().contains("Q") )
          return true;
        if(pieceColor.equals(PieceColor.BLACK.value) && castlingInfo.toString().contains("q") )
          return true;
      }
    }
    return false;
  }

  public static void updateCastlingInfo(int[][] board, Integer movedPiece,Integer color,StringBuilder castlingInfo){
    String castlingInfoString = castlingInfo.toString();
    String kValue;
    String qValue;
    int rookY;
    if(color.equals(PieceColor.WHITE.value)){
      kValue="K";
      qValue="Q";
      rookY=0;
    } else {
      kValue="k";
      qValue="q";
      rookY=7;
    }
    if(movedPiece.equals(Piece.KING.getValue())){
         castlingInfoString = castlingInfoString.replace(kValue,"").replace(qValue,"");
      if(board[0][rookY] == color + Piece.QS_ROOK.getValue() ){
        castlingInfoString = castlingInfoString.replace(qValue,"");
      }
      if(board[7][rookY] == color + Piece.QS_ROOK.getValue() ){
        castlingInfoString = castlingInfoString.replace(kValue,"");
      }
      castlingInfo.replace(0,castlingInfo.length(),castlingInfoString);
    }
  }
}
