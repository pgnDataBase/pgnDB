package com.engwork.pgndb.pgnparser;

import com.engwork.pgndb.pgnparser.consts.Consts;

/**
 * Created by Stanisław Kabaciński.
 */

public class BoardHelper implements Consts {

  private int[][] board;

  public boolean isColor(int x, int y, int color) {
    return inRange(x, y) && (board[x][y] & color) != 0;
  }

  public boolean isBlack(int x, int y) {
    return isColor(x, y, BLACK);
  }

  public boolean isWhite(int x, int y) {
    return isColor(x, y, WHITE);
  }

  public boolean isEmpty(int x, int y) {
    return inRange(x, y) && board[x][y] == 0;
  }

  public boolean inRange(int x, int y) {
    return inRange(x) && inRange(y);
  }

  public boolean inRange(int val) {
    return val >= 0 && val <= 7;
  }

  public boolean isPiece(int x, int y, int piece) {
    return board[x][y] - WHITE == piece || board[x][y] - BLACK == piece;
  }

  public static int getOppositeColor(int color) {
    return color == BLACK ? WHITE : BLACK;
  }

  public void setBoard(int[][] board) {
    this.board = board;
  }

  public static int[][] copyBoard(int[][] board) {
    int[][] newBoard = new int[8][8];
    for (int i = 0; i <= 7; ++i) {
      System.arraycopy(board[i], 0, newBoard[i], 0, 8);
    }
    return newBoard;
  }

  public int[][] getBoard() {
    return board;
  }
}
