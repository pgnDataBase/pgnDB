package com.engwork.pgndb.accessauthorization;

import com.engwork.pgndb.chessdatabases.ChessDatabase;
import com.engwork.pgndb.chessdatabases.ChessDatabaseSearchingBean;
import com.engwork.pgndb.chessdatabases.ChessDatabasesManagementBean;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class OwnershipAuthorizationBeanImpl implements OwnershipAuthorizationBean {

  private final ChessDatabaseSearchingBean chessDatabaseSearchingBean;
  private final ChessDatabasesManagementBean chessDatabasesManagementBean;

  @Override
  public Boolean validateAccess(UUID userId, UUID databaseId) {
    return chessDatabaseSearchingBean.getByUserIdAndDatabaseId(userId, databaseId) != null;
  }

  @Override
  public Boolean validateAccess(UUID userId, String databaseName) {
    ChessDatabase chessDatabase = chessDatabasesManagementBean.getChessDatabaseByName(databaseName);
    if (chessDatabase == null) {
      return false;
    }
    return validateAccess(userId, chessDatabase.getId());
  }
}
