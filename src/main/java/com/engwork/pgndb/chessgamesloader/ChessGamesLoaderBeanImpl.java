package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgamesconverter.AdditionalInfoConverter;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesconverter.model.Event;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.Player;
import com.engwork.pgndb.chessgamesconverter.model.Position;
import com.engwork.pgndb.chessgameseditor.PgnTagsMaxLengths;
import com.engwork.pgndb.statusresolver.StatusResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ChessGamesLoaderBeanImpl implements ChessGamesLoaderBean {
  private final EventsLoaderBean eventsLoaderBean;
  private final PlayersLoaderBean playersLoaderBean;
  private final MetadataLoaderBean metadataLoaderBean;
  private final PositionsLoaderBean positionsLoaderBean;
  private final MovesLoaderBean movesLoaderBean;
  private final TagsLoaderBean tagsLoaderBean;
  private final StatusResolver loadingStatusResolver;

  ChessGamesLoaderBeanImpl(EventsLoaderBean eventsLoaderBean, PlayersLoaderBean playersLoaderBean,
                           MetadataLoaderBean metadataLoaderBean, PositionsLoaderBean positionsLoaderBean,
                           MovesLoaderBean movesLoaderBean, TagsLoaderBean tagsLoaderBean,
                           StatusResolver loadingStatusResolver) {
    this.eventsLoaderBean = eventsLoaderBean;
    this.playersLoaderBean = playersLoaderBean;
    this.metadataLoaderBean = metadataLoaderBean;
    this.positionsLoaderBean = positionsLoaderBean;
    this.movesLoaderBean = movesLoaderBean;
    this.tagsLoaderBean = tagsLoaderBean;
    this.loadingStatusResolver = loadingStatusResolver;
  }

  private Map<UUID, Event> eventsMap = new HashMap<>();
  private Map<UUID, Player> whitePlayersMap = new HashMap<>();
  private Map<UUID, Player> blackPlayersMap = new HashMap<>();
  private final List<ChessGameMetadataDAO> games = new ArrayList<>();

  private Map<String, Position> positionsMap = new HashMap<>();
  private final List<Move> moves = new ArrayList<>();
  private final Set<String> tags = new HashSet<>();

  @Override
  public void loadChessGames(List<ChessGameData> chessGameDataList, String databaseName, Boolean statusMonitoring) {
    for (ChessGameData chessGameData : chessGameDataList) {
      validateAndLoad(chessGameData);
    }

    // Events
    this.eventsMap = this.eventsLoaderBean.insertWithIdCompetition(this.eventsMap, databaseName);
    updateStatus(databaseName, statusMonitoring, 0.05);
    log.info("Events successfully loaded to database {}.", databaseName);

    // Players
    this.whitePlayersMap = this.playersLoaderBean.insertWithIdCompetition(this.whitePlayersMap, databaseName);
    this.blackPlayersMap = this.playersLoaderBean.insertWithIdCompetition(this.blackPlayersMap, databaseName);
    updateStatus(databaseName, statusMonitoring, 0.05);
    log.info("Players successfully loaded to database {}.", databaseName);

    // Games
    for (ChessGameMetadataDAO chessGameMetadataDAO : this.games) {
      UUID gameId = chessGameMetadataDAO.getId();
      chessGameMetadataDAO.setEventId(eventsMap.get(gameId).getId());
      chessGameMetadataDAO.setWhitePlayer(whitePlayersMap.get(gameId).getId());
      chessGameMetadataDAO.setBlackPlayer(blackPlayersMap.get(gameId).getId());
    }
    this.metadataLoaderBean.insertMany(this.games, databaseName);
    updateStatus(databaseName, statusMonitoring, 0.1);
    log.info("Games {} successfully loaded to database {}.", this.games.size(), databaseName);

    // Tags
    this.tagsLoaderBean.insertMany(this.tags, databaseName);
    log.info("Tags successfully loaded to database {}.", databaseName);

    // Positions
    this.positionsLoaderBean.insertWithIdCompetition(this.positionsMap, databaseName);
    updateStatus(databaseName, statusMonitoring, 0.35);
    log.info("Positions {} successfully loaded to database {}.", this.positionsMap.size(), databaseName);

    // Moves
    for (Move move : this.moves) {
      if (move.getPositionId() == null)
        move.setPositionId(this.positionsMap.get(move.getFen()).getId());
    }
    this.movesLoaderBean.insertMany(this.moves, databaseName);
    updateStatus(databaseName, statusMonitoring, 0.4);
    log.info("Moves {} successfully loaded to database {}.", this.moves.size(), databaseName);

    cleanUp();
  }

  private void updateStatus(String databaseName, boolean statusMonitoring, Double multiplier) {
    if (statusMonitoring) {
      loadingStatusResolver.addByUnitWithMultiplier(databaseName, multiplier);
    }
  }

  private void validateAndLoad(ChessGameData chessGameData) {
    UUID gameId = UUID.randomUUID();
    ChessGameMetadata chessGameMetadata = chessGameData.getChessGameMetadata();
    chessGameMetadata.setGameId(gameId);

    this.validateAndLoadToGamesAndTags(gameId, chessGameMetadata, chessGameData.getGameDescription());
    this.validateAndLoadToEvents(gameId, chessGameMetadata);
    this.validateAndLoadToWhitePlayers(gameId, chessGameMetadata.getWhitePlayer());
    this.validateAndLoadToBlackPlayers(gameId, chessGameMetadata.getBlackPlayer());
    this.validateAndLoadToMoves(gameId, chessGameData.getMoveList());
    this.validateAndLoadToPositions(chessGameData.getPositionList());
  }

  private void validateAndLoadToGamesAndTags(UUID gameId, ChessGameMetadata chessGameMetadata, String description) {
    ChessGameMetadataDAO chessGameMetadataDAO = new ChessGameMetadataDAO();
    chessGameMetadataDAO.setId(gameId);
    chessGameMetadataDAO.setResult(trimString(chessGameMetadata.getResult(), PgnTagsMaxLengths.RESULT_MAX_LENGTH));
    chessGameMetadataDAO.setDescription(description);
    chessGameMetadataDAO.setRound(trimString(chessGameMetadata.getRound(), PgnTagsMaxLengths.GAME_ROUND_MAX_LENGTH));
    chessGameMetadataDAO.setDate(trimString(chessGameMetadata.getDate(), PgnTagsMaxLengths.GAME_DATE_MAX_LENGTH));
    Map<String, String> trimmedAdditionalInfo = trimStringMap(chessGameMetadata.getAdditional(), PgnTagsMaxLengths.ADDITIONAL_TAG_MAX_LENGTH);
    chessGameMetadataDAO.setAdditionalInfo(AdditionalInfoConverter.fromMapToString(trimmedAdditionalInfo));
    this.tags.addAll(trimmedAdditionalInfo.keySet());
    this.games.add(chessGameMetadataDAO);
  }

  private void validateAndLoadToEvents(UUID gameId, ChessGameMetadata chessGameMetadata) {
    Event event = new Event(null,
                            trimString(chessGameMetadata.getEvent(), PgnTagsMaxLengths.EVENT_NAME_MAX_LENGTH),
                            trimString(chessGameMetadata.getSite(), PgnTagsMaxLengths.EVENT_SITE_MAX_LENGTH)
    );
    this.eventsMap.put(gameId, event);
  }

  private void validateAndLoadToWhitePlayers(UUID gameId, String playerName) {
    Player player = new Player(null, trimString(playerName, PgnTagsMaxLengths.PLAYER_NAME_MAX_LENGTH));
    this.whitePlayersMap.put(gameId, player);
  }

  private void validateAndLoadToBlackPlayers(UUID gameId, String playerName) {
    Player player = new Player(null, trimString(playerName, PgnTagsMaxLengths.PLAYER_NAME_MAX_LENGTH));
    this.blackPlayersMap.put(gameId, player);
  }

  private void validateAndLoadToMoves(UUID gameId, List<Move> moves) {
    for (Move move : moves) {
      UUID moveId = UUID.randomUUID();
      move.setId(moveId);
      move.setGameId(gameId);
      this.moves.add(move);
    }
  }

  private void validateAndLoadToPositions(List<Position> positionList) {
    for (Position position : positionList) {
      this.positionsMap.put(position.getFen(), position);
    }
  }

  private void cleanUp() {
    this.eventsMap.clear();
    this.whitePlayersMap.clear();
    this.blackPlayersMap.clear();
    this.games.clear();
    this.positionsMap.clear();
    this.moves.clear();
    this.tags.clear();
  }

  private String trimString(String value, Integer size) {
    return value.substring(0, Math.min(value.length(), size));
  }

  private Map<String, String> trimStringMap(Map<String, String> map, Integer size) {
    for (String key : map.keySet()) {
      map.put(key, trimString(map.get(key), size));
    }
    return map;
  }
}
