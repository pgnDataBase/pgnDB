package com.engwork.pgndb.chessdatabases;

import java.util.List;
import java.util.UUID;

public interface ChessDatabaseSearchingBean {
  List<ChessDatabase> getAll();

  ChessDatabase getByUserIdAndDatabaseId(UUID userId, UUID databaseId);

  ChessDatabase getChessDatabaseByName(String databaseName);
}
