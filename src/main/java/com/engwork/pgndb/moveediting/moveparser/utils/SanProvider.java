package com.engwork.pgndb.moveediting.moveparser.utils;

import com.engwork.pgndb.chessgamesconverter.FieldUtils;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;

public class SanProvider {
  private static final String QS_CASTLING_SAN ="0-0-0";
  private static final String KS_CASTLING_SAN ="0-0";
  private static final String CAPTURE_SIGN=":";

  public static void setSanForMove(Move move , Integer movedPieceNumber, DetectedMove similarMove){
    String toAdd="";
    if(similarMove!=null){
      String sMoveStartField = FieldUtils.parseToFieldCode(similarMove.getFromX(),similarMove.getFromY());
      String dMoveStartField = move.getFromField();
      if(dMoveStartField.charAt(0)!=sMoveStartField.charAt(0)){
        toAdd+= String.valueOf(dMoveStartField.charAt(0)).toLowerCase();
      }else {
        toAdd+= String.valueOf(dMoveStartField.charAt(1)).toLowerCase();
      }
    }
    String sanPiece = Piece.getByValue(movedPieceNumber).getSanCode();
    if(move.getMoveType().equals(MoveType.MOVE.name())){
      move.setSan(sanPiece+toAdd+move.getToField().toLowerCase());
    }
    if(move.getMoveType().equals(MoveType.ATCK.name()) && !movedPieceNumber.equals(Piece.PAWN.getValue())){
      move.setSan(sanPiece+toAdd+CAPTURE_SIGN+move.getToField().toLowerCase());
    }
    if((move.getMoveType().equals(MoveType.ATCK.name())  || move.getMoveType().equals(MoveType.ENPT.name()) )&& movedPieceNumber.equals(Piece.PAWN.getValue())){
      String fieldFrom = String.valueOf(move.getFromField().charAt(0)).toLowerCase();
      move.setSan(fieldFrom+CAPTURE_SIGN+move.getToField().toLowerCase());
    }
    if(move.getMoveType().equals(MoveType.QSCG.name())){
      move.setSan(QS_CASTLING_SAN);
    }
    if(move.getMoveType().equals(MoveType.KSCG.name())){
      move.setSan(KS_CASTLING_SAN);
    }
    if(move.getPromotedPieceCode()!=null){
      Integer pieceCode = Piece.getByPieceCode(move.getPromotedPieceCode().substring(1)).getValue();
      String san = move.getSan();
      san+="="+Piece.getByValue(pieceCode).getSanCode();
      move.setSan(san);
    }
  }
}
