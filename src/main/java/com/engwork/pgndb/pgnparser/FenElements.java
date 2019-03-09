package com.engwork.pgndb.pgnparser;

import lombok.Data;

@Data
public class FenElements {
  private int[][] board;
  private Integer movesWithoutCapture;
  private String castlingInfo;

  public void boardDeepCopy(int[][] board){
    this.board = new int[8][8];
    for(int col=0;col<board.length;col++){
      for(int row=0;row < board[col].length;row++){
        this.board[col][row] = board[col][row];
      }
    }
  }
}
