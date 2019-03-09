package com.engwork.pgndb.pgnparser;

import com.engwork.pgndb.pgnparser.entities.Move;

import java.util.List;

/**
 * Created by Stanisław Kabaciński.
 */
public interface ParsedChessGame {
  void makeMove(Move move);

  void makeMove(Move move, int fromX, int fromY, int toX, int toY);

  int[][] getBoard();

  int getNextMovePlayerColor();

  boolean wasQSRookMoved(int color);

  boolean wasKSRookMoved(int color);

  boolean wasKingMoved(int color);

  boolean undoLastMove();

  void reset();

  List<Move> getMoveList();

  ParsedChessGame clone();

  String getCastlingInfo();
}
