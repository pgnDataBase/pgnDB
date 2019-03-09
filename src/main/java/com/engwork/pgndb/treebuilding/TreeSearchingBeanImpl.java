package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.chessgamesconverter.model.Position;
import com.engwork.pgndb.chessgamesloader.mappers.PositionsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.treebuilding.mapper.TreeMapper;
import com.engwork.pgndb.treebuilding.requestmodel.GetTreeNodeRequest;
import com.engwork.pgndb.treebuilding.responsemodel.TreeNodesResponse;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatus;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatusBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class TreeSearchingBeanImpl implements TreeSearchingBean {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
  private final TreeSearchingResultMappingBean treeSearchingResultMappingBean;
  private final TreeStatusBean treeStatusBean;

  public TreeNodesResponse search(GetTreeNodeRequest getTreeNodeRequest) {
    databaseContainer.setDatabaseKey(getTreeNodeRequest.getDatabaseName());

    boolean treeNotExists = !treeStatusBean.doesTreeExist(getTreeNodeRequest.getDatabaseName());
    if (treeNotExists) {
      return TreeNodesResponse.create(Collections.emptyList(), 0, new TreeStatus(false, false));
    }

    if (isSearchingByPositionId(getTreeNodeRequest)) {
      return searchByPositionId(getTreeNodeRequest);
    } else {
      return searchByFen(getTreeNodeRequest);
    }
  }

  private boolean isSearchingByPositionId(GetTreeNodeRequest getTreeNodeRequest) {
    return getTreeNodeRequest.getPositionId() != null;
  }

  private TreeNodesResponse searchByPositionId(GetTreeNodeRequest getTreeNodeRequest) {
    TreeStatus treeStatus = treeStatusBean.getTreeStatus(getTreeNodeRequest.getDatabaseName());
    List<TreeNode> treeNodes;
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      TreeMapper treeMapper = sqlSession.getMapper(TreeMapper.class);
      treeNodes = treeMapper.selectTreeNodeByStartPositionId(getTreeNodeRequest.getPositionId());
    }
    return TreeNodesResponse.create(
        treeSearchingResultMappingBean.mapToResult(treeNodes, getTreeNodeRequest),
        treeNodes.size(),
        treeStatus
    );
  }

  private TreeNodesResponse searchByFen(GetTreeNodeRequest getTreeNodeRequest) {
    TreeStatus treeStatus = treeStatusBean.getTreeStatus(getTreeNodeRequest.getDatabaseName());

    List<Position> positionIds;
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PositionsMapper positionsMapper = sqlSession.getMapper(PositionsMapper.class);
      String fen = getTreeNodeRequest.getFen().split(" ")[0];
      positionIds = positionsMapper.selectPositionsByFenRegex(fen);
    }

    if (positionIds.isEmpty()) {
      return TreeNodesResponse.create(Collections.emptyList(), 0, treeStatus);
    } else {
      List<TreeNode> treeNodes = new ArrayList<>();
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        TreeMapper treeMapper = sqlSession.getMapper(TreeMapper.class);
        positionIds.forEach(position1 -> treeNodes.addAll(treeMapper.selectTreeNodeByStartPositionId(position1.getId())));
      }
      return TreeNodesResponse.create(
          treeSearchingResultMappingBean.mapToResult(treeNodes, getTreeNodeRequest),
          treeNodes.size(),
          treeStatus
      );
    }
  }
}
