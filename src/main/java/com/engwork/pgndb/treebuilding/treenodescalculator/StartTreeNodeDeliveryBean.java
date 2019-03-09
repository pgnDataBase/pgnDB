package com.engwork.pgndb.treebuilding.treenodescalculator;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.treebuilding.TreeNode;

interface StartTreeNodeDeliveryBean {
  TreeNode deliver(Move move);
}
