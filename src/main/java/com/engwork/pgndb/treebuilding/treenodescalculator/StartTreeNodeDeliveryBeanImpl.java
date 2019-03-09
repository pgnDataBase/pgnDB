package com.engwork.pgndb.treebuilding.treenodescalculator;

import com.engwork.pgndb.chessgamesconverter.PositionBuilder;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.Position;
import com.engwork.pgndb.chessgamesloader.mappers.PositionsMapper;
import com.engwork.pgndb.treebuilding.TreeNode;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class StartTreeNodeDeliveryBeanImpl implements StartTreeNodeDeliveryBean {
  private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

  private final SqlSessionFactory sqlSessionFactory;
  private final PositionBuilder positionBuilder;

  @Override
  public TreeNode deliver(Move move) {
    UUID startPositionId;
    try (SqlSession session = sqlSessionFactory.openSession()) {
      PositionsMapper positionsMapper = session.getMapper(PositionsMapper.class);
      startPositionId = positionsMapper.selectPositionIdByFen(STARTING_FEN);
      if (startPositionId == null) {
        Position startPosition = positionBuilder.create().buildStartPosition();
        positionsMapper.insertPosition(startPosition);
        startPositionId = startPosition.getId();
      }
    }
    return new TreeNode(startPositionId, move.getPositionId(), move.getSan(), 1);
  }
}
