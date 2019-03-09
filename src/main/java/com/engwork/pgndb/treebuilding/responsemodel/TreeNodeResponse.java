package com.engwork.pgndb.treebuilding.responsemodel;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TreeNodeResponse {
  private UUID startPositionId;
  private UUID finalPositionId;
  private String fen;
  private String moveSan;
  private Integer moveCount;
  private String percent;
}
