package com.engwork.pgndb.chessgames;


import com.engwork.pgndb.chessgames.ChessGamesRequest.ChessGamesMetadataRequest;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesdownloader.GameMetadataExtracter;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
class ChessGamesBeanImpl implements ChessGamesBean {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
  private final GameMetadataExtracter gameMetadataExtracter;

  public ChessGamesMetadataResponse getChessGames(ChessGamesMetadataRequest chessGamesMetadataRequest) {
    databaseContainer.setDatabaseKey(chessGamesMetadataRequest.getDatabaseName());

    List<ChessGameMetadataDAO> chessGamesMetadataDAO;
    Integer gamesNumber;
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
      chessGamesMetadataDAO = chessGamesMapper.selectChessGames(chessGamesMetadataRequest);
      gamesNumber = chessGamesMapper.selectChessGamesNumber();
    }

    List<ChessGameMetadata> gamesMetadata= new ArrayList<>();
    for (ChessGameMetadataDAO metadataDAO : chessGamesMetadataDAO) {
      gamesMetadata.add(gameMetadataExtracter.extract(chessGamesMetadataRequest.getDatabaseName(), metadataDAO));
    }
    return new ChessGamesMetadataResponse(gamesMetadata, gamesNumber);
  }

  public Integer countChessGames(String dataSource) {
    databaseContainer.setDatabaseKey(dataSource);
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
      return chessGamesMapper.selectChessGamesNumber();
    }
  }
}
