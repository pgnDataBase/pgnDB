package com.engwork.pgndb.databasesharing;

import java.util.List;
import java.util.UUID;

public interface DatabaseSharingBean {

  boolean hasAccessTo(UUID userId, UUID databaseId);

  void addAccess(UUID ownerId, UUID databaseId, String username);

  void removeAccess(UUID databaseId, String username);

  void removeAccesses(UUID databaseId);

  void removeUserAccesses(UUID userId);

  List<String> getUsersWithAccess(UUID ownerId, UUID databaseId);
}
