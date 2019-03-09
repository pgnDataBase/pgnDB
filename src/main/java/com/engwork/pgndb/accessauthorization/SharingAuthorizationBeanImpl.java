package com.engwork.pgndb.accessauthorization;

import com.engwork.pgndb.databasesharing.DatabaseSharingBean;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class SharingAuthorizationBeanImpl implements SharingAuthorizationBean {

  private final DatabaseSharingBean databaseSharingBean;

  @Override
  public Boolean validateAccess(UUID userId, UUID databaseId) {
    return databaseSharingBean.hasAccessTo(userId, databaseId);
  }
}
