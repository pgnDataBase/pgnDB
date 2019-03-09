package com.engwork.pgndb.moveediting.moveparser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectedMove {
  private Integer fromX;
  private Integer fromY;
  private Integer toX;
  private Integer toY;
  private Integer movedPiece;
}
