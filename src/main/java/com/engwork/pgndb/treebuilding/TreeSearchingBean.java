package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.treebuilding.requestmodel.GetTreeNodeRequest;
import com.engwork.pgndb.treebuilding.responsemodel.TreeNodesResponse;

interface TreeSearchingBean {
  TreeNodesResponse search(GetTreeNodeRequest getTreeNodeRequest);
}
