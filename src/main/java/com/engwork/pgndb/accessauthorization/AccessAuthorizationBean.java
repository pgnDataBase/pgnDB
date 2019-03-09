package com.engwork.pgndb.accessauthorization;

import java.util.UUID;

public interface AccessAuthorizationBean {
  Boolean validateAccess(UUID userId, String databaseName);
  Boolean validateAccess(UUID userId, UUID databaseId);
}
