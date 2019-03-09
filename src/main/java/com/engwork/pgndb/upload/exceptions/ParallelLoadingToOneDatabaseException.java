package com.engwork.pgndb.upload.exceptions;

import com.engwork.pgndb.exceptionhandling.OperationException;

public class ParallelLoadingToOneDatabaseException extends OperationException {

  public ParallelLoadingToOneDatabaseException(String databaseName) {
    super("Games Loading", String.format("Parallel games loading to one database [%s] is not allowed!", databaseName));
  }
}