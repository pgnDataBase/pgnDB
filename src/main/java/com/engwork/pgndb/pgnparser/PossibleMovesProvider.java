package com.engwork.pgndb.pgnparser;

import java.util.List;

/**
 * Created by Stanisław Kabaciński.
 */

public interface PossibleMovesProvider {

  List<Integer> getPossibleMoves(int piece, int posX, int posY);

  void setBoard(int[][] board);

  boolean isKingChecked(int[][] board, int color);

}
