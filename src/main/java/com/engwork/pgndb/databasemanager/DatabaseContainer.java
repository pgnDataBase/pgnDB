package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.databasemanager.exceptions.DatabaseDoNotExistException;
import com.engwork.pgndb.databasemanager.exceptions.DatabaseExistsException;

import java.util.List;

public interface DatabaseContainer {

  void setDatabaseKey(String databaseName) throws DatabaseDoNotExistException;

  String getActiveDatabase();

  List<String> getAvailableDatabases();

  void addDatabaseKey(String databaseName) throws DatabaseExistsException;

  void removeDatabaseKey(String databaseName) throws DatabaseDoNotExistException;

}
