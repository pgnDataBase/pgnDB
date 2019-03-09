package com.engwork.pgndb.databasemanager.exceptions;

public class DatabaseExistsException extends RuntimeException {
  public DatabaseExistsException(String databaseName) {
    super("Database with name [" + databaseName + "] already exists!");
  }
}
