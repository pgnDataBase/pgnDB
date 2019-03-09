package com.engwork.pgndb.treebuilding.treenodescalculator;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.treebuilding.TreeNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class PlainTreeNodesCalculatorBeanImpl implements PlainTreeNodesCalculatorBean {

  private final StartTreeNodeDeliveryBean startTreeNodeDeliveryBean;

  @Override
  public Map<Key, TreeNode> calculate(List<Move> moves) {
    return calculate(moves, null);
  }

  @Override
  public Map<Key, TreeNode> calculate(List<Move> moves, UUID firstPositionId) {
    Map<Key, TreeNode> result = new HashMap<>();

    UUID currentPositionId = firstPositionId;
    for (Move currentlyProcessedMove : moves) {
      // Create new TreeNode
      TreeNode treeNode;
      if (currentPositionId == null) {
        treeNode = startTreeNodeDeliveryBean.deliver(currentlyProcessedMove);
      } else {
        treeNode = getTreeNodeForMove(currentPositionId, currentlyProcessedMove);
      }
      // Add new TreeNode to result structure
      Key key = new Key(treeNode.getStartPositionId(), treeNode.getMoveSan());
      if (result.containsKey(key)) {
        result.get(key).addToMoveCount(treeNode.getMoveCount());
      } else {
        result.put(key, treeNode);
      }
      // Update current positionId
      currentPositionId = treeNode.getFinalPositionId();
    }
    return result;
  }

  private TreeNode getTreeNodeForMove(UUID currentPositionId, Move move) {
    return new TreeNode(currentPositionId, move.getPositionId(), move.getSan(), 1);
  }
}
