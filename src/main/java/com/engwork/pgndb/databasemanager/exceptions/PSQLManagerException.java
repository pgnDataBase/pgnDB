package com.engwork.pgndb.databasemanager.exceptions;

public class PSQLManagerException extends RuntimeException {
  public PSQLManagerException(String message) {
    super("Error from PSQL Manager [" + message + "]!");
  }
}
