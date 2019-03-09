package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesloader.mappers.PlayersMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.searching.requestmodel.HeaderFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class BlackPlayerSearchingBeanImpl implements BlackPlayerSearchingBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  @Override
  @SuppressWarnings("Duplicates")
  public List<ChessGameMetadataDAO> searchByBlackPlayer(String databaseName, String blackPlayerName) {
    databaseContainer.setDatabaseKey(databaseName);
    List<ChessGameMetadataDAO> result = new ArrayList<>();
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PlayersMapper playersMapper = sqlSession.getMapper(PlayersMapper.class);
      List<UUID> playerIds = playersMapper.selectPlayerIdByNameWithRegex(blackPlayerName);
      ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
      if (!playerIds.isEmpty()) {
        result = chessGamesMapper.selectChessGamesByBlackPlayerIds(playerIds);
      }
    }
    return result;
  }

  @Override
  public List<ChessGameMetadataDAO> execute(String databaseName, HeaderFilter headerFilter) {
    return searchByBlackPlayer(databaseName, headerFilter.getBlackPlayer());
  }

  @Override
  public Boolean appliesTo(HeaderFilter headerFilter) {
    return headerFilter.getBlackPlayer() != null;
  }
}