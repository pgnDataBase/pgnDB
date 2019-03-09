package com.engwork.pgndb.databasemanager.exceptions;

import com.engwork.pgndb.exceptionhandling.OperationException;

public class DatabaseCreateManagerException extends OperationException {
  public DatabaseCreateManagerException(Throwable cause) {
    super("Database creating", cause.getMessage());
  }

  public DatabaseCreateManagerException(String databaseName, Throwable cause) {
    super("Database creating [" + databaseName + "]", cause.getMessage());
  }
}

