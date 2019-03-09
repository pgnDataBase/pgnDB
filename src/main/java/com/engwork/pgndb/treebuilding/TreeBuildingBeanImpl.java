package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesloader.mappers.MovesMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.statusresolver.StatusResolver;
import com.engwork.pgndb.treebuilding.mapper.TreeMapper;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatus;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatusBean;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class TreeBuildingBeanImpl implements TreeBuildingBean {
  private static final Integer CHUNK_SIZE = 50;

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
  private final MovesToTreeLoadingBean movesToTreeLoadingBean;
  private final TreeStatusBean treeStatusBean;
  private final StatusResolver treeBuildingStatusResolver;

  public void buildTree(String databaseName, Boolean includeVariants) {
    databaseContainer.setDatabaseKey(databaseName);
    deleteTree();
    treeStatusBean.deleteTreeStatus(databaseName);

    int offset = 0;
    int elementsProcessed = 0;
    Integer chessGamesNumber = getChessGamesNumber();
    treeBuildingStatusResolver.initStatus(databaseName, 1.0 / chessGamesNumber);
    while (offset < chessGamesNumber) {
      List<Move> moves;
      try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
        List<UUID> gameIds = sqlSession.getMapper(ChessGamesMapper.class).selectChessGamesIdsLimitOffset(CHUNK_SIZE, offset);
        moves = sqlSession.getMapper(MovesMapper.class).selectByGameIds(gameIds);
        offset += CHUNK_SIZE;
      }

      if (moves != null) {
        Map<UUID, List<Move>> movesInGames = moves.stream().collect(Collectors.groupingBy(Move::getGameId));
        for (List<Move> moveList : movesInGames.values()) {
          this.movesToTreeLoadingBean.load(moveList, includeVariants);
          elementsProcessed += 1;
          treeBuildingStatusResolver.updateStatusByKey(databaseName, ((1.0 * elementsProcessed) / (chessGamesNumber) * 100));
        }
      }
    }
    treeStatusBean.updateTreeStatus(databaseName, new TreeStatus(true, includeVariants));
    treeBuildingStatusResolver.removeByKey(databaseName);
  }

  private void deleteTree() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      session.getMapper(TreeMapper.class).deleteTree();
      session.commit();
    }
  }

  private Integer getChessGamesNumber() {
    try (SqlSession session = sqlSessionFactory.openSession()) {
      return session.getMapper(ChessGamesMapper.class).selectChessGamesNumber();
    }
  }
}