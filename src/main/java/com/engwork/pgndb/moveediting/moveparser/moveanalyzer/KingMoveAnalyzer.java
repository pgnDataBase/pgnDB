package com.engwork.pgndb.moveediting.moveparser.moveanalyzer;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;
import com.engwork.pgndb.moveediting.moveparser.utils.BoardUtils;
import com.engwork.pgndb.moveediting.moveparser.utils.PieceUtils;

public class KingMoveAnalyzer implements MoveAnalyzer {
  private static Integer KING_START_X = 4;
  private static Integer QS_CASTLING_KING_X = 2;
  private static Integer KS_CASTLING_KING_X = 6;
  private static Integer QS_CASTLING_ROOK_START_X = 0;
  private static Integer KS_CASTLING_ROOK_START_X = 7;
  private static Integer QS_CASTLING_ROOK_END_X = 3;
  private static Integer KS_CASTLING_ROOK_END_X = 5;

  public Move analyzeMove(DetectedMove detectedMove, int[][] board){
    if(isSimpleMove(detectedMove)){
      return getValidSimpleMove(detectedMove,board);
    } else {
      return getValidCastlingMove(detectedMove,board);
    }
  }

  private static Move getValidSimpleMove(DetectedMove detectedMove,int[][] board){
    Move move = new Move();
    move.setPieceCode(PieceUtils.getPieceCodeString(detectedMove.getMovedPiece()));
    Integer movedPieceColor = PieceUtils.getPieceColor(detectedMove.getMovedPiece());
    Integer capturedPiece = board[detectedMove.getToX()][detectedMove.getToY()];
    if(capturedPiece!=0){
      if(!movedPieceColor.equals(PieceUtils.getPieceColor(capturedPiece))){
        move.setMoveType(MoveType.ATCK.name());
        move.setCapturedPieceCode(PieceUtils.getPieceCodeString(capturedPiece));
        return move;
      }
    } else {
      move.setMoveType(MoveType.MOVE.name());
      return move;
    }
    return null;
  }

  private static int getStartYForKing(Integer kingPiece){
    if(PieceUtils.getPieceColor(kingPiece).equals(PieceColor.WHITE.value))
      return 0;
    else return 7;
  }

  private static boolean isRook(Integer rookPiece, Integer kingColor){
    if(rookPiece == kingColor + Piece.KS_ROOK.getValue() || rookPiece == kingColor + Piece.QS_ROOK.getValue() )
      return true;
    return false;
  }

  private static Move getValidCastlingMove(DetectedMove detectedMove,int[][] board){
    Move move = new Move();
    move.setPieceCode(PieceUtils.getPieceCodeString(detectedMove.getMovedPiece()));
    Integer kingColor = PieceUtils.getPieceColor(detectedMove.getMovedPiece());
    boolean isEmpty = BoardUtils.isEmpty(board,detectedMove.getToX(),detectedMove.getToY());
    boolean isMovePossible = BoardUtils.isMovePossible(board,detectedMove.getFromX(),detectedMove.getFromY(),detectedMove.getToX(),detectedMove.getToY()) && isEmpty;
    int startY = getStartYForKing(detectedMove.getMovedPiece());
    boolean kingOnInitialPosition = detectedMove.getFromX().equals(KING_START_X) && detectedMove.getFromY().equals(startY);
    if(kingOnInitialPosition && isMovePossible && detectedMove.getToY().equals(startY))
      return checkCastlingType(move,detectedMove,board,startY,kingColor);
    return null;
  }

  private static Move checkCastlingType(Move move, DetectedMove detectedMove,int[][] board,int startY,Integer kingColor){
    if(detectedMove.getToX().equals(QS_CASTLING_KING_X) && BoardUtils.isEmpty(board,1,startY) && isRook(board[QS_CASTLING_ROOK_START_X][startY],kingColor)){
      move.setMoveType(MoveType.QSCG.name());
      board[QS_CASTLING_ROOK_END_X][startY] = board[QS_CASTLING_ROOK_START_X][startY];
      board[QS_CASTLING_ROOK_START_X][startY]=0;
      return move;
    }
    if(detectedMove.getToX().equals(KS_CASTLING_KING_X) && isRook(board[KS_CASTLING_ROOK_START_X][startY],kingColor)){
      move.setMoveType(MoveType.KSCG.name());
      board[KS_CASTLING_ROOK_END_X][startY] = board[KS_CASTLING_ROOK_START_X][startY];
      board[KS_CASTLING_ROOK_START_X][startY]=0;
      return move;
    }
    return null;
  }

  private static boolean isSimpleMove(DetectedMove detectedMove){
    int xAbs = Math.abs(detectedMove.getFromX() - detectedMove.getToX());
    int yAbs = Math.abs(detectedMove.getFromY() -detectedMove.getToY());
    return xAbs<=1 && yAbs<=1;
  }
}
