package com.engwork.pgndb.moveediting.moveparser.moveanalyzer;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;
import com.engwork.pgndb.moveediting.moveparser.utils.BoardUtils;
import com.engwork.pgndb.moveediting.moveparser.utils.PieceUtils;

public class QBRMoveAnalyzer implements MoveAnalyzer {

  public Move analyzeMove(DetectedMove detectedMove, int[][] board){
    Integer movedPieceColor = PieceUtils.getPieceColor(detectedMove.getMovedPiece());
    Integer capturedPiece = board[detectedMove.getToX()][detectedMove.getToY()];
    Move move = new Move();
    move.setPieceCode(PieceUtils.getPieceCodeString(detectedMove.getMovedPiece()));

    if(isValidMove(detectedMove) && BoardUtils.isMovePossible(board,detectedMove.getFromX(),detectedMove.getFromY(),detectedMove.getToX(),detectedMove.getToY())){
      if(capturedPiece!=0 ) {
        if(!movedPieceColor.equals(PieceUtils.getPieceColor(capturedPiece))){
          move.setCapturedPieceCode(PieceUtils.getPieceCodeString(capturedPiece));
          move.setMoveType(MoveType.ATCK.name());
        } else
          return null;
      } else {
        move.setMoveType(MoveType.MOVE.name());
      }
    } else
      return null;

    return move;
  }


  private static boolean isValidMove(DetectedMove detectedMove){
    int xAbs = Math.abs(detectedMove.getFromX() - detectedMove.getToX());
    int yAbs = Math.abs(detectedMove.getFromY() -detectedMove.getToY());
    Integer pieceCode = PieceUtils.getPieceNumber(detectedMove.getMovedPiece());
    boolean bMovePos = xAbs  == yAbs;
    boolean rMovePos = xAbs != yAbs && (xAbs==0 || yAbs ==0);

    if(pieceCode.equals(Piece.QS_ROOK.getValue()) || pieceCode.equals(Piece.KS_ROOK.getValue()))
      return rMovePos;
    if(pieceCode.equals(Piece.BISHOP.getValue()))
      return bMovePos;
    if(pieceCode.equals(Piece.QUEEN.getValue()))
      return rMovePos || bMovePos;
    return false;
  }
}
