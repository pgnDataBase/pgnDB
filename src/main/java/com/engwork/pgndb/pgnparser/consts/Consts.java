package com.engwork.pgndb.pgnparser.consts;

/**
 * Created by Stanisław Kabaciński.
 */

public interface Consts extends PieceColor, Piece {
  int BLACK_KING = KING + BLACK;
  int BLACK_QUEEN = QUEEN + BLACK;
  int BLACK_KNIGHT = KNIGHT + BLACK;
  int BLACK_BISHOP = BISHOP + BLACK;
  int BLACK_KS_ROOK = KS_ROOK + BLACK;
  int BLACK_QS_ROOK = QS_ROOK + BLACK;
  int BLACK_PAWN = PAWN + BLACK;

  int WHITE_KING = KING + WHITE;
  int WHITE_QUEEN = QUEEN + WHITE;
  int WHITE_KNIGHT = KNIGHT + WHITE;
  int WHITE_BISHOP = BISHOP + WHITE;
  int WHITE_KS_ROOK = KS_ROOK + WHITE;
  int WHITE_QS_ROOK = QS_ROOK + WHITE;
  int WHITE_PAWN = PAWN + WHITE;
}
