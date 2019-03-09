package com.engwork.pgndb.chessengine.utils;

import com.engwork.pgndb.chessgamesconverter.FieldUtils;
import com.engwork.pgndb.chessgamesconverter.model.Field;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.moveediting.moveparser.utils.BoardUtils;
import com.engwork.pgndb.moveediting.moveparser.utils.PieceUtils;
import java.util.regex.Pattern;

class SanGenerator {
  private static final Pattern QS_CASTLING_MOVE = Pattern.compile("e\\dc\\d");
  private static final Pattern KS_CASTLING_MOVE = Pattern.compile("e\\dg\\d");
  private static final String QS_CASTLING_SAN = "0-0-0";
  private static final String KS_CASTLING_SAN = "0-0";
  private static final Integer QSC_ROOK_START_X = 0;
  private static final Integer KSC_ROOK_START_X  = 7;
  private static final Integer QSC_ROOK_END_X = 3;
  private static final Integer KSC_ROOK_END_X = 5;

  public static String parseMove(String move,int[][] board){
    Field startField = FieldUtils.parse(move.substring(0,2));
    Field finalField = FieldUtils.parse(move.substring(2));
    Integer pieceAndColor = board[startField.getXPos()][startField.getYPos()];
    Integer targetField = board[finalField.getXPos()][finalField.getYPos()];
    Integer pieceNumber = PieceUtils.getPieceNumber(pieceAndColor);
    if(pieceNumber.equals(Piece.PAWN.getValue())) {
      return pawnMoveSAN(startField,finalField,move,targetField,board);
    }
    if(pieceNumber.equals(Piece.KING.getValue()))
      return kingMoveSAN(startField,finalField,move,targetField,board);
    return otherPiecesMoveSAN(startField,finalField,move,targetField,board);
  }
  private static String otherPiecesMoveSAN(Field startField,Field finalField,String move,Integer targetField,int[][] board){
    Integer pieceAndColor = board[startField.getXPos()][startField.getYPos()];
    Integer pieceNumber = PieceUtils.getPieceNumber(pieceAndColor);
    executeMove(startField,finalField,board);
    String toAdd =getCollisionSign(startField,finalField,board,pieceAndColor,pieceNumber);
    return Piece.getByValue(pieceNumber).getSanCode()+toAdd+getCaptureInfo(targetField)+finalField.getCode().toLowerCase();
  }

  private static String getCollisionSign(Field startField, Field finalField, int[][] board, Integer pieceAndColor, Integer pieceNumber) {
    if (!pieceNumber.equals(Piece.QUEEN.getValue())) {
      Field samePiece = BoardUtils.findSamePiece(board, startField.getXPos(), startField.getYPos(), pieceAndColor);
      if (samePiece != null && BoardUtils.isValidNRBMove(board, samePiece.getXPos(), samePiece.getYPos(), finalField.getXPos(), finalField.getYPos())) {
        if (!startField.getXPos().equals(samePiece.getXPos()))
          return Character.toString(startField.getCode().charAt(0));
        else
          return Character.toString(startField.getCode().charAt(1));
      }
    }
    return "";
  }
  private static String getCaptureInfo(Integer targetField) {
    if(!targetField.equals(0))
      return "x";
    return "";
  }

  private static void executeMove(Field startField,Field finalField, int[][] board){
    executeMove(startField.getXPos(),startField.getYPos(),finalField.getXPos(),finalField.getYPos(),board);
  }

  private static void executeMove(int startX, int startY, int endX, int endY, int[][] board){
    Integer pieceValue = board[startX][startY];
    board[startX][startY] = 0;
    board[endX][endY] = pieceValue;
  }
  private static String kingMoveSAN(Field startField,Field finalField,String move,Integer targetField,int[][] board){
    Integer kingStartY = startField.getYPos();
    executeMove(startField,finalField,board);
    if(QS_CASTLING_MOVE.matcher(move).find()) {
      executeMove(QSC_ROOK_START_X,kingStartY,QSC_ROOK_END_X,kingStartY,board);
      return QS_CASTLING_SAN;
    }
    if (KS_CASTLING_MOVE.matcher(move).find()) {
      executeMove(KSC_ROOK_START_X,kingStartY,KSC_ROOK_END_X,kingStartY,board);
      return KS_CASTLING_SAN;
    }
    return Piece.KING.getSanCode()+getCaptureInfo(targetField)+finalField.getCode().toLowerCase();
  }

  private static String pawnMoveSAN(Field startField,Field finalField,String move,Integer targetField,int[][] board) {
    String captureSign = "";
    boolean isCapturingMove = !startField.getXPos().equals(finalField.getXPos());
    executeMove(startField,finalField,board);
    if(isCapturingMove) {
      captureSign = Character.toString(move.charAt(0))+"x";
      // En passant
      if(targetField.equals(0))
        board[finalField.getXPos()][startField.getYPos()] = 0;
    }
    return captureSign+finalField.getCode().toLowerCase();
  }
}
