package com.engwork.pgndb.searching.exceptions;

import com.engwork.pgndb.exceptionhandling.OperationException;

public class FilteringByStartFenNotSupportedException extends OperationException {
  public FilteringByStartFenNotSupportedException() {
    super("Games searching with position filter", "Filtering with starting position [rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w] is not supported!" );
  }
}