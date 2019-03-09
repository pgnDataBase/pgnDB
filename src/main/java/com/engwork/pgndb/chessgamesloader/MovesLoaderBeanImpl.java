package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesloader.mappers.MovesMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@Slf4j
class MovesLoaderBeanImpl implements MovesLoaderBean {
  private static final Integer CHUNK_SIZE = 500;
  private static final Integer CHUNKS_NUMBER = 20;

  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  MovesLoaderBeanImpl(SqlSessionFactory sqlSessionFactory, DatabaseContainer databaseContainer) {
    this.sqlSessionFactory = sqlSessionFactory;
    this.databaseContainer = databaseContainer;
  }

  @Override
  @SuppressWarnings("Duplicates")
  public void insertMany(List<Move> moves, String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    int movesInserted = 0;
    List<Move> chunk = new ArrayList<>();

    SqlSession sqlSession = sqlSessionFactory.openSession();
    MovesMapper movesMapper = sqlSession.getMapper(MovesMapper.class);
    try {
      for (int index = 0; index < moves.size(); index++) {
        chunk.add(moves.get(index));
        boolean isChunkFull = chunk.size() == CHUNK_SIZE;
        boolean areElementsProcessedAndChunkNotEmpty = (index == moves.size() - 1) && !chunk.isEmpty();
        if (isChunkFull || areElementsProcessedAndChunkNotEmpty) {
          movesMapper.insertMany(chunk);
          movesInserted += chunk.size();
          chunk.clear();
          log.debug("Moves loading {}/{}.", movesInserted, moves.size());
        }
      }
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }
}
