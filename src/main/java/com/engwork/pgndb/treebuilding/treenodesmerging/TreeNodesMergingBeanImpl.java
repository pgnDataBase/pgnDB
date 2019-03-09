package com.engwork.pgndb.treebuilding.treenodesmerging;

import com.engwork.pgndb.treebuilding.TreeNode;
import java.util.List;

class TreeNodesMergingBeanImpl implements TreeNodesMergingBean {
  @Override
  public List<TreeNode> merge(List<TreeNode> oldTreeNodes, List<TreeNode> newTreeNodes) {
    newTreeNodes.forEach(node -> {
      int index = oldTreeNodes.indexOf(node);
      if (index != -1) {
        oldTreeNodes.get(index).addToMoveCount(node.getMoveCount());
      } else {
        oldTreeNodes.add(node);
      }
    });
    return oldTreeNodes;
  }
}
