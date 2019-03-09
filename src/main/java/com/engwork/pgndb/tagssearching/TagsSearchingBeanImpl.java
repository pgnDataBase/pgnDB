package com.engwork.pgndb.tagssearching;

import com.engwork.pgndb.chessgamesloader.mappers.TagsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.util.List;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class TagsSearchingBeanImpl implements TagsSearchingBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;
  private final SortWithStartValueBean sortWithStartValue;

  @Override
  public List<String> search(SearchTagsRequest searchTagsRequest) {
    databaseContainer.setDatabaseKey(searchTagsRequest.getDatabaseName());
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      TagsMapper tagsMapper = sqlSession.getMapper(TagsMapper.class);
      List<String> result = tagsMapper.selectTagsWithRegex(searchTagsRequest.getValue());
      return sortWithStartValue.sort(result, searchTagsRequest.getValue());
    }
  }
}
