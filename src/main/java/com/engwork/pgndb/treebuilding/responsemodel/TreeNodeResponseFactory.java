package com.engwork.pgndb.treebuilding.responsemodel;

import com.engwork.pgndb.treebuilding.TreeNode;

public interface TreeNodeResponseFactory {
  TreeNodeResponse fromTreeNode(TreeNode treeNode, Integer quantity, String databaseName);
}
