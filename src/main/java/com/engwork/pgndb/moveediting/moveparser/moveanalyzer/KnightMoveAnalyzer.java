package com.engwork.pgndb.moveediting.moveparser.moveanalyzer;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;
import com.engwork.pgndb.moveediting.moveparser.utils.PieceUtils;

public class KnightMoveAnalyzer implements MoveAnalyzer {

    public Move analyzeMove(DetectedMove detectedMove, int[][] board){
      if(isValidMove(detectedMove)){
        return getValidMove(detectedMove,board);
      }
      return null;
    }

    private static Move getValidMove(DetectedMove detectedMove, int[][] board){
      Integer movedPieceColor = PieceUtils.getPieceColor(detectedMove.getMovedPiece());
      Integer capturedPiece = board[detectedMove.getToX()][detectedMove.getToY()];
      Move move = new Move();
      move.setPieceCode(PieceUtils.getPieceCodeString(detectedMove.getMovedPiece()));

      if(capturedPiece!=0 ) {
        if(!movedPieceColor.equals(PieceUtils.getPieceColor(capturedPiece))){
          move.setCapturedPieceCode(PieceUtils.getPieceCodeString(capturedPiece));
          move.setMoveType(MoveType.ATCK.name());
          return move;
        }
      } else {
        move.setMoveType(MoveType.MOVE.name());
        return move;
      }
      return move;
    }


    private static boolean isValidMove(DetectedMove detectedMove){
      int xAbs = Math.abs(detectedMove.getFromX() - detectedMove.getToX());
      int yAbs = Math.abs(detectedMove.getFromY() -detectedMove.getToY());
      return ((xAbs==2 && yAbs==1) || (xAbs==1 && yAbs==2));
    }
}