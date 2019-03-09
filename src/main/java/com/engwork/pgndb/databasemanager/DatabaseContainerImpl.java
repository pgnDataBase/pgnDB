package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.databasemanager.exceptions.DatabaseDoNotExistException;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseExistsException;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseNotChosenException;

import java.util.ArrayList;
import java.util.List;

class DatabaseContainerImpl implements DatabaseContainer {

  private final ThreadLocal<String> databaseThreadLocal = new ThreadLocal<>();

  private final List<String> databases = new ArrayList<>();

  public synchronized void setDatabaseKey(String databaseName) throws DatabaseDoNotExistException {
    if (this.containsDatabaseKey(databaseName)) {
      databaseThreadLocal.set(databaseName);
    } else {
      throw new DatabaseDoNotExistException(databaseName);
    }
  }

  public synchronized String getActiveDatabase() {
    String databaseName = databaseThreadLocal.get();
    if (databaseName != null) {
      return databaseName;
    } else {
      throw new DatabaseNotChosenException();
    }
  }

  public synchronized List<String> getAvailableDatabases() {
    return databases;
  }

  public synchronized void addDatabaseKey(String databaseName) throws DatabaseExistsException {
    if (!this.containsDatabaseKey(databaseName)) {
      databases.add(databaseName);
    } else {
      throw new DatabaseExistsException(databaseName);
    }
  }

  public synchronized void removeDatabaseKey(String databaseName) throws DatabaseDoNotExistException {
    if (this.containsDatabaseKey(databaseName)) {
      databases.remove(databaseName);
      if (databaseName.equals(databaseThreadLocal.get())) {
        databaseThreadLocal.set(null);
      }
    } else {
      throw new DatabaseDoNotExistException(databaseName);
    }
  }

  private boolean containsDatabaseKey(String databaseName) {
    return databases.contains(databaseName);
  }

}
