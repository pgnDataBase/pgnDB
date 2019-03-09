package com.engwork.pgndb.chessdatabases;

import com.engwork.pgndb.chessdatabases.mappers.ChessDatabasesMapper;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class ChessDatabaseSearchingBeanImpl implements ChessDatabaseSearchingBean {

  private final ChessDatabasesMapper chessDatabasesMapper;

  @Override
  public List<ChessDatabase> getAll() {
    return chessDatabasesMapper.selectChessDatabases();
  }

  @Override
  public ChessDatabase getByUserIdAndDatabaseId(UUID userId, UUID databaseId) {
    return chessDatabasesMapper.selectChessDatabaseByUserIdAndDatabaseId(userId, databaseId);
  }

  @Override
  public ChessDatabase getChessDatabaseByName(String databaseName) {
    return chessDatabasesMapper.selectChessDatabasesByName(databaseName);
  }
}
