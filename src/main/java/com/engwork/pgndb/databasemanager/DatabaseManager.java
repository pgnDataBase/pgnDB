package com.engwork.pgndb.databasemanager;

import java.util.UUID;

public interface DatabaseManager {

  void create(String databaseName, UUID userId);

  void delete(String databaseName, boolean force);
}
