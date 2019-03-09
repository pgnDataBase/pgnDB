package com.engwork.pgndb.databasemanager;

import java.util.UUID;

interface PSQLManager {

  void createDatabase(String databaseName, UUID userId);

  void deleteDatabase(String databaseName, boolean force);
}
