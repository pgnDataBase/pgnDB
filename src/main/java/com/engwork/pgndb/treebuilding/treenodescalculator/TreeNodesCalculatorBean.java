package com.engwork.pgndb.treebuilding.treenodescalculator;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.treebuilding.TreeNode;

import java.util.List;

public interface TreeNodesCalculatorBean {
  List<TreeNode> calculate(List<Move> moves, Boolean includeVariants);
}
