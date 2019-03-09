package com.engwork.pgndb.treebuilding.treenodescalculator;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.treebuilding.TreeNode;

import java.util.List;
import java.util.Map;

interface VariantTreeNodesCalculatorBean {
  Map<Key, TreeNode> calculate(List<Move> moves);
}
