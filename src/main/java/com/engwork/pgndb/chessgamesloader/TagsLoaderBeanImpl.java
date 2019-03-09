package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesloader.mappers.TagsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.util.ArrayList;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
public class TagsLoaderBeanImpl implements TagsLoaderBean {
  private static final Integer CHUNK_SIZE = 500;

  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  @Override
  @SuppressWarnings("Duplicates")
  public void insertMany(Set<String> tags, String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);
    try {
      int index = 0;
      ArrayList<String> chunk = new ArrayList<>();
      for (String tag : tags) {
        chunk.add(tag);
        boolean isChunkFull = chunk.size() == CHUNK_SIZE;
        boolean areElementsProcessedAndChunkNotEmpty = (index == tags.size() - 1) && !chunk.isEmpty();
        if (isChunkFull || areElementsProcessedAndChunkNotEmpty) {
          tagsMapper.insertMany(chunk);
          chunk.clear();
        }
        index += 1;
      }
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }
}
