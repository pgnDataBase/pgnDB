package com.engwork.pgndb.databasemanager.exceptions;

public class DatabaseDoNotExistException extends RuntimeException {
  public DatabaseDoNotExistException(String databaseName) {
    super("Database with name [" + databaseName + "] doesn't exist!");
  }
}
