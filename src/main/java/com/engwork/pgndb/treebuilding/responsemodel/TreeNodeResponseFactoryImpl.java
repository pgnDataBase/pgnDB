package com.engwork.pgndb.treebuilding.responsemodel;

import com.engwork.pgndb.treebuilding.TreeNode;
import java.text.DecimalFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class TreeNodeResponseFactoryImpl implements TreeNodeResponseFactory {

  public TreeNodeResponse fromTreeNode(TreeNode treeNode, Integer quantity, String fen) {
    return new TreeNodeResponse(
        treeNode.getStartPositionId(),
        treeNode.getFinalPositionId(),
        fen,
        treeNode.getMoveSan(),
        treeNode.getMoveCount(),
        getPercent(treeNode.getMoveCount(), quantity)
    );
  }

  private String getPercent(Integer moveCount, Integer quantity) {
    Double percent = (((1.0 * moveCount) / quantity) * 100);
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    return decimalFormat.format(percent) + "%";
  }
}
