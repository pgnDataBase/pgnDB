package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.chessgamesconverter.model.Position;
import com.engwork.pgndb.chessgamesloader.mappers.PositionsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.treebuilding.requestmodel.GetTreeNodeRequest;
import com.engwork.pgndb.treebuilding.responsemodel.TreeNodeResponse;
import com.engwork.pgndb.treebuilding.responsemodel.TreeNodeResponseFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class TreeSearchingResultMappingBeanImpl implements TreeSearchingResultMappingBean {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
  private final TreeNodeResponseFactory treeNodeResponseFactory;

  @Override
  public List<TreeNodeResponse> mapToResult(List<TreeNode> treeNodes, GetTreeNodeRequest getTreeNodeRequest) {
    Integer countSum = treeNodes.stream().map(TreeNode::getMoveCount).mapToInt(Integer::intValue).sum();
    List<Position> positions = getPositionsForTreeNodes(treeNodes, getTreeNodeRequest.getDatabaseName());
    Map<UUID, String> fensMap = positions.stream().collect(Collectors.toMap(Position::getId, Position::getFen));

    List<TreeNodeResponse> nodeResponses;
    nodeResponses = treeNodes
        .stream()
        .map(treeNode -> treeNodeResponseFactory.fromTreeNode(treeNode, countSum, fensMap.get(treeNode.getFinalPositionId())))
        .collect(Collectors.toList());
    return sortAndApplyLimitOffset(nodeResponses, getTreeNodeRequest);
  }

  private List<TreeNodeResponse> sortAndApplyLimitOffset(List<TreeNodeResponse> nodeResponses, GetTreeNodeRequest getTreeNodeRequest) {
    nodeResponses = nodeResponses.stream().sorted(Comparator.comparing(TreeNodeResponse::getMoveCount)).collect(Collectors.toList());
    Collections.reverse(nodeResponses);
    Integer limit = getTreeNodeRequest.getLimit();
    Integer offset = getTreeNodeRequest.getOffset();
    if (offset > nodeResponses.size() - 1) {
      return Collections.emptyList();
    }

    List<TreeNodeResponse> resultWithLimitAndOffset = new ArrayList<>();
    for (int index = offset; index < offset + limit; index++) {
      if (index > nodeResponses.size() - 1) {
        break;
      }
      resultWithLimitAndOffset.add(nodeResponses.get(index));
    }
    return resultWithLimitAndOffset;
  }

  private List<Position> getPositionsForTreeNodes(List<TreeNode> treeNodes, String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    List<UUID> ids = treeNodes.stream().map(TreeNode::getFinalPositionId).collect(Collectors.toList());
    if (ids.isEmpty()) {
      return Collections.emptyList();
    } else {
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        PositionsMapper positionsMapper = sqlSession.getMapper(PositionsMapper.class);
        return positionsMapper.selectPositionsByIds(ids);
      }
    }
  }

}
