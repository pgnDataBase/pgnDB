package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.treebuilding.requestmodel.GetTreeNodeRequest;
import com.engwork.pgndb.treebuilding.responsemodel.TreeNodeResponse;

import java.util.List;

interface TreeSearchingResultMappingBean {
  List<TreeNodeResponse> mapToResult(List<TreeNode> treeNodes, GetTreeNodeRequest getTreeNodeRequest);
}
