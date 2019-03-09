package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.searching.requestmodel.HeaderFilter;
import java.util.List;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class DateSearchingBeanImpl implements DateSearchingBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  @Override
  public List<ChessGameMetadataDAO> searchByDate(String databaseName, String date) {
    databaseContainer.setDatabaseKey(databaseName);
    List<ChessGameMetadataDAO> result;
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
      result = chessGamesMapper.selectChessGamesByDate(date);
    }
    return result;
  }

  @Override
  public List<ChessGameMetadataDAO> execute(String databaseName, HeaderFilter headerFilter) {
    return searchByDate(databaseName, headerFilter.getDate());
  }

  @Override
  public Boolean appliesTo(HeaderFilter headerFilter) {
    return headerFilter.getDate() != null;
  }
}
