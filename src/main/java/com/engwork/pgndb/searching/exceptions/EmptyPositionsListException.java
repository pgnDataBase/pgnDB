package com.engwork.pgndb.searching.exceptions;

import com.engwork.pgndb.exceptionhandling.OperationException;
import com.engwork.pgndb.searching.requestmodel.PositionsFilter;

public class EmptyPositionsListException extends OperationException {
  public EmptyPositionsListException(PositionsFilter positionsFilter) {
    super("Games searching", "List of positions cannot be empty: " + positionsFilter.toString());
  }
}