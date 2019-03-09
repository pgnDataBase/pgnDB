package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.Position;
import com.engwork.pgndb.chessgamesloader.mappers.PositionsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@Slf4j
@AllArgsConstructor
class PositionsLoaderBeanImpl implements PositionsLoaderBean {
  private static final Integer LOAD_CHUNK_SIZE = 500;
  private static final Integer READ_CHUNK_SIZE = 1000;
  private static final Integer ALLOW_CHUNK_READ_RATIO = 50;

  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  @Override
  public Map<String, Position> insertWithIdCompetition(Map<String, Position> positionsMap, String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    SqlSession sqlSession = sqlSessionFactory.openSession();
    PositionsMapper positionsMapper = sqlSession.getMapper(PositionsMapper.class);
    try {
      Integer positionsNumber = positionsMapper.countPositions();
      if (positionsNumber < ALLOW_CHUNK_READ_RATIO * positionsMap.size()) {
//        Chunk read existing positions
        log.info("Using chunk read for positions. Existing {}, to insert {}.", positionsNumber, positionsMap.size());
        Integer offset = 0;
        while (offset < positionsNumber) {
          List<Position> positions = positionsMapper.selectPositionsLimitOffset(READ_CHUNK_SIZE, offset);
          for (Position position : positions) {
            UUID id = position.getId();
            String fen = position.getFen();
            if (positionsMap.containsKey(fen)) {
              positionsMap.get(fen).setId(id);
            }
          }
          offset += READ_CHUNK_SIZE;
        }
      } else {
//        Single read for each new position
        log.info("Using single position read for positions. Existing {}, to insert {}.", positionsNumber, positionsMap.size());
        for (Position position : positionsMap.values()) {
          UUID positionId = positionsMapper.selectPositionId(position);
          if (positionId != null) {
            position.setId(positionId);
          }
        }
      }

//      Chunk load new positions
      List<Position> chunk = new ArrayList<>();
      int index = 0;
      int newPositions = 0;
      for (Position position : positionsMap.values()) {
        if (position.getId() == null) {
          position.setId(UUID.randomUUID());
          newPositions += 1;
          chunk.add(position);
        }
        boolean isChunkFull = chunk.size() == LOAD_CHUNK_SIZE;
        boolean areElementsProcessedAndChunkNotEmpty = (index == positionsMap.size() - 1) && !chunk.isEmpty();
        if (isChunkFull || areElementsProcessedAndChunkNotEmpty) {
          positionsMapper.insertMany(chunk);
          log.debug("Positions loading {}/{}.", newPositions, positionsMap.size() - (index + 1 - newPositions));
          chunk.clear();
        }
        index++;
      }
      log.debug("New positions inserted: {}. Skipped: {}", newPositions, positionsMap.size() - newPositions);
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
    return positionsMap;
  }
}
