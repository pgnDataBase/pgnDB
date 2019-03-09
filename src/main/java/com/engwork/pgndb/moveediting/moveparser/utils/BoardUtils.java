package com.engwork.pgndb.moveediting.moveparser.utils;

import com.engwork.pgndb.chessgamesconverter.FieldUtils;
import com.engwork.pgndb.chessgamesconverter.model.Field;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardUtils {

  private static int[][] createBoard(){
    int[][] board =new int[8][8];
    for(int[] row : board)
      Arrays.fill(row,0);
    return board;
  }

  public static int[][] getBoardFromFen(String fen){
    int[][] board =createBoard();
    String position = fen.split(" ")[0];
    String[] fenRows = position.split("/");
    for (int row = 0; row < board.length; row++) {
      int col =0;
      String fenRow = fenRows[ Math.abs(row-7)];
      for(char piece : fenRow.toCharArray()){
        if(!Character.isDigit(piece)){
          board[col][row]=getPieceNumberFromFenCode(piece);
          col++;
        } else {
          col += Character.getNumericValue(piece);
        }
      }
    }
    return board;
  }

  private static Integer getPieceNumberFromFenCode(char piece){
    Integer pieceNumber = Piece.getByFenCode(Character.toLowerCase(piece)).getValue();
    if(Character.isUpperCase(piece))
      return PieceColor.WHITE.getValue() + pieceNumber;
    else
      return PieceColor.BLACK.getValue() + pieceNumber;
  }

  private static int getDirection(int from, int to){
    int direction =  Integer.compare(to, from);
    return (int) Math.signum(direction);
  }

  public static boolean isMovePossible(int[][] board,int fromX ,int fromY, int toX, int toY){
    int xDir = getDirection(fromX,toX);
    int yDir = getDirection(fromY,toY);
    int xPos=fromX+xDir;
    int yPos=fromY+yDir;
    while ( xPos!=toX || yPos!=toY){
      if(board[xPos][yPos]!=0)
        return false;
      xPos+=xDir;
      yPos+=yDir;
    }
    return true;
  }

  public static boolean isValidNRBMove(int[][] board, int startX, int startY, int endX,int endY) {
    int xAbs = Math.abs(startX - endX);
    int yAbs = Math.abs(startY - endY);
    Integer pieceAndColor = board[startX][startY];
    Integer pieceValue = PieceUtils.getPieceColor(pieceAndColor);
    boolean isRookMove = xAbs == 0 || yAbs == 0;
    boolean isBishopMove = xAbs == yAbs;
    boolean isKnightMove = (xAbs == 1 && yAbs ==2) || (xAbs == 2 && yAbs ==1);
    boolean isRook = pieceValue.equals(Piece.QS_ROOK.getValue()) || pieceValue.equals(Piece.KS_ROOK.getValue());
    if(pieceValue.equals(Piece.KNIGHT.getValue()) && isKnightMove)
      return true;
    if(isRook && isRookMove && isMovePossible(board,startX,startY,endX,endY))
      return true;
    if(pieceValue.equals(Piece.BISHOP.getValue()) && isBishopMove && isMovePossible(board,startX,startY,endX,endY))
      return true;
    return false;
  }
  public static boolean isEmpty(int[][] board,int x ,int y){
    return board[x][y]==0;
  }

  public static boolean contains(int[][] board,int x ,int y,int value){
    return board[x][y]==value;
  }

  public static List<DetectedMove> findSimilarMoves(int[][] board, DetectedMove detectedMove){
    List<DetectedMove> similarMoves = new ArrayList<>();
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++)
        if((col!=detectedMove.getFromX() || row!=detectedMove.getFromY() ) && board[col][row]==detectedMove.getMovedPiece()){
          DetectedMove newDetectedMove = new DetectedMove();
          newDetectedMove.setFromX(col);
          newDetectedMove.setFromY(row);
          newDetectedMove.setMovedPiece(detectedMove.getMovedPiece());
          newDetectedMove.setToX(detectedMove.getToX());
          newDetectedMove.setToY(detectedMove.getToY());
          similarMoves.add(newDetectedMove);
        }
    }
    return similarMoves;
  }

  public static Field findSamePiece(int[][] board, int xPos, int yPos, int value){
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++)
        if((col != xPos || row != yPos)  && board[col][row]==value){
          String field =  FieldUtils.parseToFieldCode(col,row);
          return FieldUtils.parse(field);
        }
    }
    return null;
  }
}
