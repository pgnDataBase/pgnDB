package com.engwork.pgndb.moveediting.moveparser.moveanalyzer;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;
import com.engwork.pgndb.moveediting.moveparser.utils.BoardUtils;
import com.engwork.pgndb.moveediting.moveparser.utils.PieceUtils;

public class PawnMoveAnalyzer implements MoveAnalyzer {

  private static Integer WHITE_START_Y = 1;
  private static Integer BLACK_START_Y = 6;


  public Move analyzeMove(DetectedMove detectedMove, int[][] board){
    if(isValidDirectionForPiece(detectedMove.getMovedPiece(),detectedMove.getFromY(),detectedMove.getToY())) {
      if (!detectedMove.getFromX().equals(detectedMove.getToX()))
        return getValidAttackingMove(detectedMove, board);
      else
        return getValidSimpleMove(detectedMove, board);
    } else{
      return null;
    }
  }

  private static Integer getStartYForPiece(Integer piece){
    if(PieceUtils.getPieceColor(piece).equals(PieceColor.WHITE.value))
      return WHITE_START_Y;
    else
      return BLACK_START_Y;
  }

  private static Boolean isValidDirectionForPiece(Integer piece, Integer fromY,Integer toY){
    if(PieceUtils.getPieceColor(piece).equals(PieceColor.WHITE.value))
      return fromY < toY;
    else
      return fromY > toY;
  }

  private static Integer getEnemyPiece(Integer piece){
    if(PieceUtils.getPieceColor(piece).equals(PieceColor.WHITE.value))
      return PieceColor.BLACK.value + Piece.PAWN.getValue();
    else
      return PieceColor.WHITE.value+ Piece.PAWN.getValue();
  }

  private static Move getValidAttackingMove(DetectedMove detectedMove, int[][] board){
    Integer fromX = detectedMove.getFromX();
    Integer fromY = detectedMove.getFromY();
    Integer toX = detectedMove.getToX();
    Integer toY = detectedMove.getToY();
    Integer movedPiece = detectedMove.getMovedPiece();
    Integer movedPieceColor = PieceUtils.getPieceColor(movedPiece);
    Integer capturedPiece=board[toX][toY];

    Move move = new Move();
    move.setPieceCode(PieceUtils.getPieceCodeString(movedPiece));
    //En passant
    if(Math.abs(fromY - toY)== 1 && Math.abs(fromX - toX)== 1 && BoardUtils.isEmpty(board,toX,toY) && BoardUtils.contains(board,toX,fromY,getEnemyPiece(movedPiece))) {
      capturedPiece = getEnemyPiece(movedPiece);
      move.setMoveType(MoveType.ENPT.name());
      move.setCapturedPieceCode(PieceUtils.getPieceCodeString(capturedPiece));
      board[toX][fromY] = 0;
      return move;
    }
    //Simple attacking move
    if(Math.abs(fromY - toY)== 1 && Math.abs(fromX - toX)== 1 && capturedPiece!=0 &&!PieceUtils.getPieceColor(capturedPiece).equals(movedPieceColor)) {
      move.setCapturedPieceCode(PieceUtils.getPieceCodeString(capturedPiece));
      move.setMoveType(MoveType.ATCK.name());
      return move;
    }
    return null;
  }

  private static Move getValidSimpleMove(DetectedMove detectedMove, int[][] board){
    Integer fromX = detectedMove.getFromX();
    Integer fromY = detectedMove.getFromY();
    Integer toX = detectedMove.getToX();
    Integer toY = detectedMove.getToY();
    Integer movedPiece = detectedMove.getMovedPiece();

    Move move = new Move();
    move.setPieceCode(PieceUtils.getPieceCodeString(movedPiece));
    move.setMoveType(MoveType.MOVE.name());

    if(Math.abs(fromY - toY)== 1 && BoardUtils.isEmpty(board,toX,toY))
      return move;
    if(Math.abs(fromY - toY)== 2 && BoardUtils.isEmpty(board,toX,toY) && fromY.equals(getStartYForPiece(movedPiece)) && BoardUtils.isMovePossible(board,fromX,fromY,toX,toY))
      return move;
    return null;
  }
}
