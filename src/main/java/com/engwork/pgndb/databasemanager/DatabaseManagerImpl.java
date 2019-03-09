package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.chessdatabases.ChessDatabase;
import com.engwork.pgndb.chessdatabases.ChessDatabaseSearchingBean;
import com.engwork.pgndb.chessdatabases.databasevalidation.DatabaseValidationBean;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseCreateManagerException;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseDeleteManagerException;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseDoNotExistException;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseExistsException;
import com.engwork.pgndb.databasemanager.exceptions.PSQLManagerException;
import com.engwork.pgndb.databasemanager.exceptions.UserDoesNotExistException;
import com.engwork.pgndb.databasesharing.DatabaseSharingBean;
import com.engwork.pgndb.users.UsersBean;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class DatabaseManagerImpl implements DatabaseManager {

  private final DynamicDataSource dataSource;
  private final ChessDatabaseSearchingBean chessDatabaseSearchingBean;
  private final DatabaseSharingBean databaseSharingBean;
  private final PSQLManager psqlManager;
  private final DatabaseContainer databaseContainer;
  private final DynamicDataSourceBuilder dynamicDataSourceBuilder;
  private final UsersBean usersBean;
  private final DatabaseValidationBean databaseValidationBean;

  @Override
  public synchronized void create(String databaseName, UUID userId) throws DatabaseCreateManagerException {
    databaseValidationBean.validateName(databaseName);

    //Check if user exists
    if (!usersBean.checkIfUserExists(userId)) {
      log.info("User with id [{}] doesn't exist.", userId);
      throw new UserDoesNotExistException("Database creation", userId);
    }

    try {
      // Create actual database in PSQL database
      psqlManager.createDatabase(databaseName, userId);
      // Add to databases container
      databaseContainer.addDatabaseKey(databaseName);
      // Add to data sources collection for dynamic session
      addToDataSources(databaseName);
      log.info("Database [{}] successfully created.", databaseName);
    } catch (DatabaseExistsException exception) {
      throw new DatabaseCreateManagerException(exception);
    } catch (PSQLManagerException exception) {
      throw new DatabaseCreateManagerException(databaseName, exception);
    }
  }

  private void addToDataSources(String databaseName) {
    Map<Object, Object> dataSourceMap = new HashMap<>();
    DataSource newDataSource = dynamicDataSourceBuilder.build(databaseName);
    dataSourceMap.put(databaseName, newDataSource);
    for (ChessDatabase chessDatabase : chessDatabaseSearchingBean.getAll()) {
      DataSource dataSource = dynamicDataSourceBuilder.build(chessDatabase.getName());
      dataSourceMap.put(chessDatabase.getName(), dataSource);
    }
    this.dataSource.setTargetDataSources(dataSourceMap);
    this.dataSource.afterPropertiesSet();
  }

  @Override
  public synchronized void delete(String databaseName, boolean force) throws DatabaseDeleteManagerException {

    try {
      ChessDatabase chessDatabase = chessDatabaseSearchingBean.getChessDatabaseByName(databaseName);
      UUID databaseId = null;
      if (chessDatabase != null) {
        databaseId = chessDatabase.getId();
      }

      // Remove actual database in PSQL database
      psqlManager.deleteDatabase(databaseName, force);
      // Remove from database container
      databaseContainer.removeDatabaseKey(databaseName);
      // Remove from  data sources collection for dynamic session
      removeFromDataSources(databaseName);
      // Remove sharing for database
      databaseSharingBean.removeAccesses(databaseId);
      log.info("Database [{}] successfully deleted.", databaseName);
    } catch (DatabaseDoNotExistException exception) {
      throw new DatabaseDeleteManagerException(exception);
    } catch (PSQLManagerException exception) {
      throw new DatabaseDeleteManagerException(databaseName, exception);
    }
  }

  private void removeFromDataSources(String databaseName) {
    Map<Object, Object> dataSourceMap = new HashMap<>();
    for (ChessDatabase chessDatabase : chessDatabaseSearchingBean.getAll()) {
      if (chessDatabase.getName().equals(databaseName)) {
        continue;
      }
      DataSource dataSource = dynamicDataSourceBuilder.build(chessDatabase.getName());
      dataSourceMap.put(chessDatabase.getName(), dataSource);
    }
    this.dataSource.setTargetDataSources(dataSourceMap);
    this.dataSource.afterPropertiesSet();
  }

}
