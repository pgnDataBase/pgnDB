package com.engwork.pgndb.accessauthorization;

import java.util.UUID;

public interface SharingAuthorizationBean {
  Boolean validateAccess(UUID userId, UUID databaseId);
}
