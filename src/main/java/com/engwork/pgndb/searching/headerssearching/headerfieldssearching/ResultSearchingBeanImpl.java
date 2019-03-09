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
class ResultSearchingBeanImpl implements ResultSearchingBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  @Override
  public List<ChessGameMetadataDAO> searchByResult(String databaseName, String gameResult) {
    databaseContainer.setDatabaseKey(databaseName);
    List<ChessGameMetadataDAO> result;
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
      result = chessGamesMapper.selectChessGamesByResult(gameResult);
    }
    return result;
  }

  @Override
  public List<ChessGameMetadataDAO> execute(String databaseName, HeaderFilter headerFilter) {
    return searchByResult(databaseName, headerFilter.getResult());
  }

  @Override
  public Boolean appliesTo(HeaderFilter headerFilter) {
    return headerFilter.getResult() != null;
  }
}
