package com.engwork.pgndb.moveediting;


import com.engwork.pgndb.chessgamesconverter.PositionBuilder;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.Position;
import com.engwork.pgndb.chessgamesloader.ChessGamesLoaderBean;
import com.engwork.pgndb.chessgamesloader.mappers.MovesMapper;
import com.engwork.pgndb.chessgamesloader.mappers.PositionsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatus;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatusBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class MoveSavingBeanImpl implements MoveSavingBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;
  private final MoveValidatingBean moveValidatingBean;
  private final PositionBuilder positionBuilder;
  private final ChessGamesLoaderBean chessGamesLoaderBean;
  private final TreeStatusBean treeStatusBean;

  private static final Boolean STATUS_MONITORING_ACTIVE = false;

  public SaveChangesResponse saveChanges(SaveChangesRequest saveChangesRequest) {
    databaseContainer.setDatabaseKey(saveChangesRequest.getDatabaseName());
    ChessGameData chessGameData = saveChangesRequest.getChessGameData();
    UUID gameId = chessGameData.getChessGameMetadata().getGameId();
    List<Move> moveList = saveChangesRequest.getChessGameData().getMoveList();
    Integer movesCount = getMovesCount(gameId);
    processVariantEndEntities(moveList);
    validateMoveList(moveList, gameId);
    updatePositions(moveList);
    if (chessGameData.getNotPersisted() == null || !chessGameData.getNotPersisted())
      insertMoves(moveList, gameId);
    else {
      List<ChessGameData> chessGameDataList = new ArrayList<>();
      chessGameDataList.add(chessGameData);
      chessGamesLoaderBean.loadChessGames(chessGameDataList, saveChangesRequest.getDatabaseName(), STATUS_MONITORING_ACTIVE);
      gameId = chessGameDataList.get(0).getChessGameMetadata().getGameId();
    }
    updateTreeStatus(moveList, saveChangesRequest.getDatabaseName(), movesCount);
    return new SaveChangesResponse(gameId);
  }

  private void updateTreeStatus(List<Move> moveList, String databaseName, Integer movesCount) {
    if (treeStatusBean.doesTreeExist(databaseName)) {
      TreeStatus treeStatus = treeStatusBean.getTreeStatus(databaseName);
      boolean anyAddedMove = moveList.stream().anyMatch(move -> move.getPositionId() == null);
      boolean anyAddedVariantMove = moveList.stream().anyMatch(move -> move.getPositionId() == null && move.isVariant());
      boolean moveListChanged = !movesCount.equals(moveList.size());
      if (anyAddedMove || moveListChanged || (anyAddedVariantMove && treeStatus.getIsTreeWithVariants())) {
        treeStatusBean.setTreeNotUpToDate(databaseName);
      }
    }
  }

  private void insertMoves(List<Move> moveList, UUID gameId) {
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    MovesMapper movesMapper = sqlSession.getMapper(MovesMapper.class);
    try {
      movesMapper.deleteMovesByGameId(gameId);
      movesMapper.insertMany(moveList);
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }

  private Integer getMovesCount(UUID gameId) {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    MovesMapper movesMapper = sqlSession.getMapper(MovesMapper.class);
    Integer movesCount;
    try {
      movesCount = movesMapper.selectMovesCountByGameId(gameId);
    } finally {
      sqlSession.close();
    }
    return movesCount;
  }

  private Map<String, Position> getPositions(List<String> fenList) {
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    PositionsMapper positionsMapper = sqlSession.getMapper(PositionsMapper.class);
    Map<String, Position> positionsMap;
    try {
      positionsMap = positionsMapper.selectByFens(fenList).stream().collect(Collectors.toMap(Position::getFen, item -> item));
    } finally {
      sqlSession.close();
    }
    return positionsMap;
  }

  private void insertPositions(List<Position> positionList) {
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    PositionsMapper positionsMapper = sqlSession.getMapper(PositionsMapper.class);
    try {
      positionsMapper.insertMany(positionList);
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }

  private void updatePositions(List<Move> moveList) {
    List<String> fenList = moveList.stream().filter(move -> move.getPositionId() == null).map(Move::getFen).collect(Collectors.toList());
    if (fenList.isEmpty())
      return;
    Map<String, Position> positionsMap = getPositions(fenList);
    List<Position> newPositions = new ArrayList<>();
    for (String fen : fenList) {
      if (!positionsMap.containsKey(fen)) {
        Position position = positionBuilder.create().buildFromFen(fen);
        position.setId(UUID.randomUUID());
        newPositions.add(position);
      }
    }
    newPositions.forEach(position -> positionsMap.put(position.getFen(), position));
    if (newPositions.size() != 0)
      insertPositions(newPositions);
    for (Move move : moveList) {
      if (positionsMap.containsKey(move.getFen())) {
        move.setPositionId(positionsMap.get(move.getFen()).getId());
      }
    }
  }

  private void processVariantEndEntities(List<Move> moveList) {
    List<Move> variantEndings = moveList.stream().filter(move -> move.getVariantType() != null && move.getVariantType().equals("VE")).collect(Collectors.toList());
    for (Move move : variantEndings) {
      move.setId(UUID.randomUUID());
      move.setSan("-");
      move.setFromField("-");
      move.setToField("-");
      move.setPieceCode("-");
      move.setMoveNumber(0);
      move.setMoveType(MoveType.VEME.name());
      move.setPositionId(UUID.randomUUID());
      move.setFen("-");
    }
  }

  private void validateMoveList(List<Move> moveList, UUID gameId) {
    List<Move> filteredMoves = moveList.stream().filter(move -> !move.getMoveType().equals(MoveType.VEME.name())).collect(Collectors.toList());
    moveValidatingBean.validateMovesGroup(filteredMoves.stream().filter(move -> !move.isVariant()).collect(Collectors.toList()));
    Map<Integer, List<Move>> moveGroups = filteredMoves.stream().filter(Move::isVariant).collect(Collectors.groupingBy(Move::getVariantId));
    for (List<Move> moves : moveGroups.values()) {
      moveValidatingBean.validateMovesGroup(moves);
    }
    Integer entityNumber = 1;
    for (Move move : moveList) {
      move.setEntityNumber(entityNumber);
      move.setGameId(gameId);
      entityNumber++;
    }
  }
}
