package com.engwork.pgndb.databasemanager.exceptions;

import com.engwork.pgndb.exceptionhandling.OperationException;

public class DatabaseDeleteManagerException extends OperationException {
  public DatabaseDeleteManagerException(Throwable cause) {
    super("Database deleting", cause.getMessage());
  }

  public DatabaseDeleteManagerException(String databaseName, Throwable cause) {
    super("Database deleting [" + databaseName + "]", cause.getMessage());
  }
}
