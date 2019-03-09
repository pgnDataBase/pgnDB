package com.engwork.pgndb.treebuilding;

import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TreeNode {
  private UUID startPositionId;
  private UUID finalPositionId;
  private String moveSan;
  private Integer moveCount;

  public void addToMoveCount(int value) {
    this.moveCount += value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    TreeNode treeNode = (TreeNode) object;
    return Objects.equals(startPositionId, treeNode.startPositionId) &&
        Objects.equals(moveSan, treeNode.moveSan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startPositionId, moveSan);
  }
}
