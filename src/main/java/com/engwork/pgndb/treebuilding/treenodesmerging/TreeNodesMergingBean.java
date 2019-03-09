package com.engwork.pgndb.treebuilding.treenodesmerging;

import com.engwork.pgndb.treebuilding.TreeNode;
import java.util.List;

public interface TreeNodesMergingBean {
  List<TreeNode> merge(List<TreeNode> oldTreeNodes, List<TreeNode> newTreeNodes);
}
