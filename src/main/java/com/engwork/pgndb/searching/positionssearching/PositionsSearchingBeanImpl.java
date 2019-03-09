package com.engwork.pgndb.searching.positionssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.Position;
import com.engwork.pgndb.chessgamesloader.mappers.MovesMapper;
import com.engwork.pgndb.chessgamesloader.mappers.PositionsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.searching.requestmodel.PositionsFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class PositionsSearchingBeanImpl implements PositionsSearchingBean {

  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;
  private final PositionsFilterValidatorBean positionsFilterValidatorBean;

  @Override
  public List<ChessGameMetadataDAO> search(String databaseName, PositionsFilter positionsFilter) {
    databaseContainer.setDatabaseKey(databaseName);
    positionsFilterValidatorBean.validate(positionsFilter);

    List<String> fens = positionsFilter.getPositions();
    Boolean includeVariants = positionsFilter.getIncludeVariants();

    List<Position> positions = getPositionsByFens(fens);
    boolean positionsNotInDatabase = positions.size() != fens.size();
    if (positionsNotInDatabase) {
      return Collections.emptyList();
    }

    List<Move> moves = getMovesByPositions(positions, includeVariants);

    Map<UUID, List<Move>> groupedMoves = groupMovesByGameId(moves);

    return getValidGamesByGroupedMoves(groupedMoves, positions.size());
  }

  private List<Position> getPositionsByFens(List<String> fens) {
    List<Position> result = new ArrayList<>();
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PositionsMapper positionsMapper = sqlSession.getMapper(PositionsMapper.class);
      for (String fen : fens) {
        List<Position> positions = positionsMapper.selectPositionsByFenRegex(fen);
        if (!positions.isEmpty()) {
          result.addAll(positions);
        }
      }
    }
    return result;
  }

  private List<Move> getMovesByPositions(List<Position> positions, Boolean includeVariants) {
    if (includeVariants) {
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        MovesMapper movesMapper = sqlSession.getMapper(MovesMapper.class);
        return movesMapper.selectByPositionIds(positions.stream().map(Position::getId).collect(Collectors.toList()));
      }
    } else {
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        MovesMapper movesMapper = sqlSession.getMapper(MovesMapper.class);
        return movesMapper.selectByPositionIdsExcludeVariants(positions.stream().map(Position::getId).collect(Collectors.toList()));
      }
    }
  }

  private Map<UUID, List<Move>> groupMovesByGameId(List<Move> moves) {
    return moves.stream().collect(Collectors.groupingBy(Move::getGameId));
  }

  private List<ChessGameMetadataDAO> getValidGamesByGroupedMoves(Map<UUID, List<Move>> groupedMoves, Integer numberOfPositionsRequired) {
    List<UUID> validGamesIds = new ArrayList<>();
    for (UUID groupId : groupedMoves.keySet()) {
      boolean containsAllPositionsRequired = groupedMoves.get(groupId).stream().map(Move::getPositionId).distinct().count() == numberOfPositionsRequired;
      if (containsAllPositionsRequired) {
        validGamesIds.add(groupId);
      }
    }
    List<ChessGameMetadataDAO> result = new ArrayList<>();
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ChessGamesMapper gamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
      for (UUID gameId : validGamesIds) {
        result.add(gamesMapper.selectChessGameById(gameId));
      }
    }
    return result;
  }
}
