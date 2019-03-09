package com.engwork.pgndb.moveediting.moveparser.moveanalyzer;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;

public interface MoveAnalyzer {
  Move analyzeMove(DetectedMove detectedMove, int[][] board);
}
