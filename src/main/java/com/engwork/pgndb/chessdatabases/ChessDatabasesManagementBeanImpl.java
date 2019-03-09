package com.engwork.pgndb.chessdatabases;

import com.engwork.pgndb.chessdatabases.mappers.ChessDatabasesMapper;
import com.engwork.pgndb.chessgames.ChessGamesBean;
import com.engwork.pgndb.databasesharing.DatabaseAccess;
import com.engwork.pgndb.databasesharing.mappers.DatabasesSharingMapper;
import com.engwork.pgndb.users.UserMetadata;
import com.engwork.pgndb.users.mappers.UsersMapper;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class ChessDatabasesManagementBeanImpl implements ChessDatabasesManagementBean {

  private final ChessDatabasesMapper chessDatabasesMapper;
  private final ChessGamesBean chessGamesBean;
  private final UsersMapper usersMapper;
  private final DatabasesSharingMapper databasesSharingMapper;

  @Override
  public List<ChessDatabase> getChessDatabases() {
    List<ChessDatabase> result = chessDatabasesMapper.selectChessDatabases();
    for (ChessDatabase chessDatabase : result) {
      Integer gamesTotal = chessGamesBean.countChessGames(chessDatabase.getName());
      chessDatabase.setGamesTotal(gamesTotal);
    }
    return result;
  }

  @Override
  public List<ChessDatabase> getChessDatabasesByUserId(UUID userId) {
    UserMetadata userMetadata = usersMapper.selectUserMetadataByUserId(userId);

    List<ChessDatabase> result = chessDatabasesMapper.selectChessDatabasesByUserId(userId);
    result.forEach(chessDatabase -> {
      chessDatabase.setIsShared(false);
      chessDatabase.setOwnerUsername(userMetadata.getUsername());
    });

    List<DatabaseAccess> resultShared = databasesSharingMapper.selectByUserId(userId);
    resultShared
        .forEach(databaseAccess -> {
          ChessDatabase chessDatabase = chessDatabasesMapper.selectChessDatabasesById(databaseAccess.getDatabaseId());
          chessDatabase.setIsShared(true);
          chessDatabase.setOwnerUsername(usersMapper.selectUserMetadataByUserId(databaseAccess.getOwnerId()).getUsername());
          result.add(chessDatabase);
        });

    for (ChessDatabase chessDatabase : result) {
      Integer gamesTotal = chessGamesBean.countChessGames(chessDatabase.getName());
      chessDatabase.setGamesTotal(gamesTotal);
    }
    return result;
  }

  @Override
  public ChessDatabase getChessDatabaseByName(String databaseName) {
    return chessDatabasesMapper.selectChessDatabasesByName(databaseName);
  }

  @Override
  public void insertChessDatabase(String databaseName, UUID userId) {
    chessDatabasesMapper.insertChessDatabase(UUID.randomUUID(), databaseName, userId);
  }

  @Override
  public void deleteChessDatabase(String databaseName) {
    chessDatabasesMapper.deleteChessDatabase(databaseName);
  }
}
