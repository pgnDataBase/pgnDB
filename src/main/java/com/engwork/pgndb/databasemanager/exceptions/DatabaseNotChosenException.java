package com.engwork.pgndb.databasemanager.exceptions;

public class DatabaseNotChosenException extends RuntimeException {
  public DatabaseNotChosenException() {
    super("Database for thread is not chosen!");
  }
}
