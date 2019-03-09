package com.engwork.pgndb.searching.exceptions;

import com.engwork.pgndb.exceptionhandling.OperationException;

public class MissingPositionsListException extends OperationException {
  public MissingPositionsListException() {
    super("Games searching", "List of positions cannot be null!");
  }
}