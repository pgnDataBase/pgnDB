package com.engwork.pgndb.treebuilding.treenodescalculator;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.treebuilding.TreeNode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class VariantTreeNodesCalculatorBeanImpl implements VariantTreeNodesCalculatorBean {

  private final PlainTreeNodesCalculatorBean plainTreeNodesCalculatorBean;

  @Override
  public Map<Key, TreeNode> calculate(List<Move> moves) {
    Map<Key, TreeNode> result = new HashMap<>();
    // Sort moves
    moves = moves
        .stream()
        .sorted(Comparator.comparing(Move::getEntityNumber))
        .collect(Collectors.toList());

    // Create needed data structures
    Map<Integer, List<Move>> movesForVariant = new HashMap<>();

    // Find moves and start position for each variant
    for (Move currentlyProcessedMove : moves) {
      if (currentlyProcessedMove.isNotNullMove()) {
        if (currentlyProcessedMove.isVariant()) {
          if (movesForVariant.containsKey(currentlyProcessedMove.getVariantId())) {
            movesForVariant.get(currentlyProcessedMove.getVariantId()).add(currentlyProcessedMove);
          } else {
            ArrayList<Move> movesForConcreteVariant = new ArrayList<>();
            movesForConcreteVariant.add(currentlyProcessedMove);
            movesForVariant.put(currentlyProcessedMove.getVariantId(), movesForConcreteVariant);
          }
        }
      }
    }

    // Calculate tree nodes for each variant
    for (List<Move> movesForConcreteVariant : movesForVariant.values()) {
      // Find variant parent positionId
      Move firstMove = movesForConcreteVariant.get(0);
      Move parentMove = findVariantParentMove(moves, firstMove);
      UUID parentPositionId;
      if (parentMove == null) {
        parentPositionId = null;
      } else {
        parentPositionId = parentMove.getPositionId();
      }

      // Calculate TreeNodes for variant
      Map<Key, TreeNode> newNodes;
      newNodes = plainTreeNodesCalculatorBean.calculate(movesForConcreteVariant, parentPositionId);
      // Merge new TreeNodes with result
      for (Key key : newNodes.keySet()) {
        if (result.containsKey(key)) {
          result.get(key).addToMoveCount(newNodes.get(key).getMoveCount());
        } else {
          result.put(key, newNodes.get(key));
        }
      }
    }
    return result;
  }

  private Move findVariantParentMove(List<Move> moves, Move firstMoveInVariant) {

    List<Move> candidates = moves
        .stream()
        .filter(move -> move.getEntityNumber() < firstMoveInVariant.getEntityNumber())
        .sorted(Comparator.comparing(Move::getEntityNumber, Comparator.reverseOrder()))
        .collect(Collectors.toList());

    List<Integer> variantsBlackList = new ArrayList<>();
    Integer expectedMoveNumber = firstMoveInVariant.getMoveNumber() - 1;
    for (Move move : candidates) {
      if (!move.isNotNullMove()) {
        variantsBlackList.add(move.getVariantId());
      }
      if (expectedMoveNumber.equals(move.getMoveNumber()) && !variantsBlackList.contains(move.getVariantId())) {
        return move;
      }
    }
    return null;
  }
}
