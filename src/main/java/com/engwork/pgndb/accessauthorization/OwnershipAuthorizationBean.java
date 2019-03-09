package com.engwork.pgndb.accessauthorization;

import java.util.UUID;

public interface OwnershipAuthorizationBean {
  Boolean validateAccess(UUID userId, UUID databaseId);
  Boolean validateAccess(UUID userId, String databaseName);
}
