package com.engwork.pgndb.chessgameseditor;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgames.DeleteGamesRequest;
import com.engwork.pgndb.chessgamesconverter.AdditionalInfoConverter;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesconverter.model.Event;
import com.engwork.pgndb.chessgamesconverter.model.Player;
import com.engwork.pgndb.chessgamesloader.mappers.EventsMapper;
import com.engwork.pgndb.chessgamesloader.mappers.MovesMapper;
import com.engwork.pgndb.chessgamesloader.mappers.PlayersMapper;
import com.engwork.pgndb.chessgamesloader.mappers.PositionsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.exceptionhandling.ValidationException;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Map;
import java.util.UUID;

public class ChessGamesEditorBeanImpl implements ChessGamesEditorBean {
  private EventsMapper eventsMapper;
  private PlayersMapper playersMapper;
  private ChessGamesMapper chessGamesMapper;
  private MovesMapper movesMapper;
  private PositionsMapper positionsMapper;
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;


  public ChessGamesEditorBeanImpl(DatabaseContainer databaseContainer,SqlSessionFactory sqlSessionFactory){
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
  }
  private void initMappers(SqlSession sqlSession) {
    movesMapper = sqlSession.getMapper(MovesMapper.class);
    positionsMapper = sqlSession.getMapper(PositionsMapper.class);
    eventsMapper = sqlSession.getMapper(EventsMapper.class);
    playersMapper = sqlSession.getMapper(PlayersMapper.class);
    chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
  }

  private void validate(String fieldName, String value,Integer maxCharacters){
    if(value.length()>maxCharacters){
      String reason = "Field "+fieldName+" contains more than "+maxCharacters.toString()+" characters";
      throw ValidationException.forSingleObjectAndFieldAndReason("ChessGameMetadata",fieldName,reason);
    }
  }

  public void updateChessGameMetadata(String databaseName, ChessGameMetadata chessGameMetadata){
    databaseContainer.setDatabaseKey(databaseName);
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    initMappers(sqlSession);
    try {
      ChessGameMetadataDAO chessGameMetadataDAO = chessGamesMapper.selectChessGameById(chessGameMetadata.getGameId());
      modifyPlayers(chessGameMetadata,chessGameMetadataDAO,sqlSession);
      modifyEvent(chessGameMetadata,chessGameMetadataDAO,sqlSession);

      chessGameMetadataDAO.setResult(chessGameMetadata.getResult());
      validate("result",chessGameMetadata.getResult(),PgnTagsMaxLengths.RESULT_MAX_LENGTH);

      chessGameMetadataDAO.setRound(chessGameMetadata.getRound());
      validate("round",chessGameMetadata.getRound(),PgnTagsMaxLengths.GAME_ROUND_MAX_LENGTH);

      chessGameMetadataDAO.setDate(chessGameMetadata.getDate());
      validate("date",chessGameMetadata.getDate(),PgnTagsMaxLengths.GAME_DATE_MAX_LENGTH);

      updateAdditionalTags(chessGameMetadata,chessGameMetadataDAO);
      chessGamesMapper.updateChessGame(chessGameMetadataDAO);
      sqlSession.commit();
    }
    finally {
      sqlSession.close();
    }
  }
  private void updateAdditionalTags(ChessGameMetadata chessGameMetadata, ChessGameMetadataDAO chessGameMetadataDAO){
    Map<String,String> newAdditional = chessGameMetadata.getAdditional();
    for(String key : newAdditional.keySet()) {
      validate(key,newAdditional.get(key),PgnTagsMaxLengths.ADDITIONAL_TAG_MAX_LENGTH);
    }
    chessGameMetadataDAO.setAdditionalInfo(AdditionalInfoConverter.fromMapToString(newAdditional));
  }

  private void modifyPlayers(ChessGameMetadata chessGameMetadata,ChessGameMetadataDAO chessGameMetadataDAO,SqlSession sqlSession){
    String whitePlayerName = chessGameMetadata.getWhitePlayer();
    validate("whitePlayer",whitePlayerName,PgnTagsMaxLengths.PLAYER_NAME_MAX_LENGTH);

    String blackPlayerName = chessGameMetadata.getBlackPlayer();
    validate("blackPlayer",blackPlayerName,PgnTagsMaxLengths.PLAYER_NAME_MAX_LENGTH);

    chessGameMetadataDAO.setWhitePlayer(getNewPlayerId(whitePlayerName,sqlSession));
    chessGameMetadataDAO.setBlackPlayer(getNewPlayerId(blackPlayerName,sqlSession));
  }
  private void modifyEvent(ChessGameMetadata chessGameMetadata,ChessGameMetadataDAO chessGameMetadataDAO,SqlSession sqlSession){
    String eventName = chessGameMetadata.getEvent();
    validate("event",eventName,PgnTagsMaxLengths.EVENT_NAME_MAX_LENGTH);

    String site = chessGameMetadata.getSite();
    validate("site",site,PgnTagsMaxLengths.EVENT_SITE_MAX_LENGTH);

    Event event= new Event(null,eventName,site);
    UUID eventId = eventsMapper.selectEventId(event);
    if(eventId!=null){
      chessGameMetadataDAO.setEventId(eventId);
    } else {
      event.setId(UUID.randomUUID());
      eventsMapper.insertEvent(event);
      chessGameMetadataDAO.setEventId(event.getId());
    }
  }
  private UUID getNewPlayerId(String playerName,SqlSession sqlSession){
    UUID playerId = playersMapper.selectPlayerIdByName(playerName);
    if(playerId!=null){
      return playerId;
    } else {
      Player player = new Player(UUID.randomUUID(),playerName);
      playersMapper.insertPlayer(player);
      return player.getId();
    }
  }

  public void deleteGames(DeleteGamesRequest deleteGamesRequest){
    databaseContainer.setDatabaseKey(deleteGamesRequest.getDatabaseName());
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    initMappers(sqlSession);
    try {
      for(UUID gameId : deleteGamesRequest.getGamesIds()){
        movesMapper.deleteMovesByGameId(gameId);
        chessGamesMapper.deleteChessGameById(gameId);
      }
      sqlSession.commit();
    } finally {
      sqlSession.close();
    }
  }
}
