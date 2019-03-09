package com.engwork.pgndb.chessdatabases;

import java.util.List;
import java.util.UUID;

public interface ChessDatabasesManagementBean {

  List<ChessDatabase> getChessDatabases();

  List<ChessDatabase> getChessDatabasesByUserId(UUID userId);

  ChessDatabase getChessDatabaseByName(String databaseName);

  void insertChessDatabase(String databaseName, UUID userId);

  void deleteChessDatabase(String databaseName);
}
