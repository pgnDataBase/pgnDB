package com.engwork.pgndb.pgnparser;

import com.engwork.pgndb.pgnparser.consts.Piece;
import com.engwork.pgndb.pgnparser.consts.PieceColor;

/**
 * Created by Stanisław Kabaciński.
 */

public class BoardUtils implements PieceColor {

  public static void initBoard(int[][] board) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = 0;
      }
    }
    for (int i = 0, ii = 8; i < ii; i++) {
      board[i][1] = WHITE + Piece.PAWN;
      board[i][6] = BLACK + Piece.PAWN;
    }
    board[0][0] = WHITE + Piece.QS_ROOK;
    board[7][0] = WHITE + Piece.KS_ROOK;
    board[1][0] = board[6][0] = WHITE + Piece.KNIGHT;
    board[2][0] = board[5][0] = WHITE + Piece.BISHOP;
    board[3][0] = WHITE + Piece.QUEEN;
    board[4][0] = WHITE + Piece.KING;

    board[0][7] = BLACK + Piece.QS_ROOK;
    board[7][7] = BLACK + Piece.KS_ROOK;
    board[1][7] = board[6][7] = BLACK + Piece.KNIGHT;
    board[2][7] = board[5][7] = BLACK + Piece.BISHOP;
    board[3][7] = BLACK + Piece.QUEEN;
    board[4][7] = BLACK + Piece.KING;
  }
}
