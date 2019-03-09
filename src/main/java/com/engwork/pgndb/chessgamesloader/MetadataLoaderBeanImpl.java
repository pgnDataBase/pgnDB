package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

class MetadataLoaderBeanImpl implements MetadataLoaderBean {
  private static final Integer CHUNK_SIZE = 500;

  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  MetadataLoaderBeanImpl(SqlSessionFactory sqlSessionFactory, DatabaseContainer databaseContainer) {
    this.sqlSessionFactory = sqlSessionFactory;
    this.databaseContainer = databaseContainer;
  }

  @Override
  @SuppressWarnings("Duplicates")
  public void insertMany(List<ChessGameMetadataDAO> metadata, String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
    try {
      ArrayList<ChessGameMetadataDAO> chunk = new ArrayList<>();
      for (int index = 0; index < metadata.size(); index++) {
        chunk.add(metadata.get(index));
        boolean isChunkFull = chunk.size() == CHUNK_SIZE;
        boolean areElementsProcessedAndChunkNotEmpty = (index == metadata.size() - 1) && !chunk.isEmpty();
        if (isChunkFull || areElementsProcessedAndChunkNotEmpty) {
          chessGamesMapper.insertMany(chunk);
          chunk.clear();
        }
      }
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }
}
