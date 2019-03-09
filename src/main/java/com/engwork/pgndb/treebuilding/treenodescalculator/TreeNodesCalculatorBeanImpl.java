package com.engwork.pgndb.treebuilding.treenodescalculator;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.treebuilding.TreeNode;
import com.engwork.pgndb.treebuilding.treenodesmerging.TreeNodesMergingBean;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class TreeNodesCalculatorBeanImpl implements TreeNodesCalculatorBean {

  private final TreeNodesMergingBean treeNodesMergingBean;
  private final PlainTreeNodesCalculatorBean plainTreeNodeCalculatorBean;
  private final VariantTreeNodesCalculatorBean variantTreeNodeCalculatorBean;

  @Override
  public List<TreeNode> calculate(List<Move> moves, Boolean includeVariants) {
    if (includeVariants) {
      return treeNodesMergingBean.merge(
          calculatePlain(moves),
          new ArrayList<>(variantTreeNodeCalculatorBean.calculate(moves).values())
      );
    } else {
      return calculatePlain(moves);
    }
  }

  private List<TreeNode> calculatePlain(List<Move> moves) {
    // Sort moves and filter variants
    moves = moves
        .stream()
        .filter(Move::isNotNullMove)
        .filter(move -> !move.isVariant())
        .sorted(Comparator.comparing(Move::getEntityNumber))
        .collect(Collectors.toList());
    return new ArrayList<>(plainTreeNodeCalculatorBean.calculate(moves).values());
  }
}