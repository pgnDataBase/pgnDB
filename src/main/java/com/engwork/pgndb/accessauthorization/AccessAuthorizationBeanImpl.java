package com.engwork.pgndb.accessauthorization;

import com.engwork.pgndb.chessdatabases.ChessDatabase;
import com.engwork.pgndb.chessdatabases.ChessDatabasesManagementBean;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class AccessAuthorizationBeanImpl implements AccessAuthorizationBean {

  private final OwnershipAuthorizationBean ownershipAuthorizationBean;
  private final SharingAuthorizationBean sharingAuthorizationBean;
  private final ChessDatabasesManagementBean chessDatabasesManagementBean;

  @Override
  public Boolean validateAccess(UUID userId, String databaseName) {

    ChessDatabase chessDatabase = chessDatabasesManagementBean.getChessDatabaseByName(databaseName);
    if (chessDatabase == null) {
      return false;
    }
    return validateAccess(userId, chessDatabase.getId());
  }

  @Override
  public Boolean validateAccess(UUID userId, UUID databaseId) {
    boolean isOwner = ownershipAuthorizationBean.validateAccess(userId, databaseId);
    boolean hasShared = sharingAuthorizationBean.validateAccess(userId, databaseId);

    return isOwner || hasShared;
  }
}
