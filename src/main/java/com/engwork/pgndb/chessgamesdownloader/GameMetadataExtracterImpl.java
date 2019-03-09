package com.engwork.pgndb.chessgamesdownloader;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgamesconverter.AdditionalInfoConverter;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesconverter.model.Event;
import com.engwork.pgndb.chessgamesconverter.model.Player;
import com.engwork.pgndb.chessgamesloader.mappers.EventsMapper;
import com.engwork.pgndb.chessgamesloader.mappers.PlayersMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class GameMetadataExtracterImpl implements GameMetadataExtracter {

  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  @Override
  public ChessGameMetadata extract(String databaseName, ChessGameMetadataDAO chessGameMetadataDAO) {
    databaseContainer.setDatabaseKey(databaseName);

    ChessGameMetadata chessGameMetadata = new ChessGameMetadata();

    chessGameMetadata.setGameId(chessGameMetadataDAO.getId());
    Event event;
    Player whitePlayer;
    Player blackPlayer;
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

      EventsMapper eventsMapper = sqlSession.getMapper(EventsMapper.class);
      PlayersMapper playersMapper = sqlSession.getMapper(PlayersMapper.class);

      whitePlayer = playersMapper.selectPlayerById(chessGameMetadataDAO.getWhitePlayer());
      blackPlayer = playersMapper.selectPlayerById(chessGameMetadataDAO.getBlackPlayer());
      event = eventsMapper.selectEventById(chessGameMetadataDAO.getEventId());

    }

    chessGameMetadata.setEvent(event.getName());
    chessGameMetadata.setSite(event.getPlace());
    chessGameMetadata.setWhitePlayer(whitePlayer.getName());
    chessGameMetadata.setBlackPlayer(blackPlayer.getName());
    chessGameMetadata.setDate(chessGameMetadataDAO.getDate());
    chessGameMetadata.setResult(chessGameMetadataDAO.getResult());
    chessGameMetadata.setAdditional(AdditionalInfoConverter.fromStringToMap(chessGameMetadataDAO.getAdditionalInfo()));
    chessGameMetadata.setRound(chessGameMetadataDAO.getRound());
    return chessGameMetadata;
  }
}
