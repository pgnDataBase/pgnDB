package com.engwork.pgndb.chessgamesdownloader;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesloader.mappers.MovesMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class ChessGamesDownloaderBeanImpl implements ChessGamesDownloaderBean {
  private ChessGamesMapper gamesMapper;
  private MovesMapper movesMapper;
  private DatabaseContainer databaseContainer;
  private GameMetadataExtracter gameMetadataExtracter;

  @Override
  public ChessGameData getChessGameDataById(UUID id, String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    ChessGameData chessGameData = new ChessGameData();

    ChessGameMetadataDAO chessGameMetadataDAO = gamesMapper.selectChessGameById(id);

    ChessGameMetadata chessGameMetadata = gameMetadataExtracter.extract(databaseName, chessGameMetadataDAO);
    chessGameData.setChessGameMetadata(chessGameMetadata);

    List<Move> moveList = movesMapper.selectMovesByGameId(id);
    chessGameData.setMoveList(moveList);

    return chessGameData;
  }
}
