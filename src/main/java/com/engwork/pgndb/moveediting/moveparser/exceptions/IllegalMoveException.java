package com.engwork.pgndb.moveediting.moveparser.exceptions;

import com.engwork.pgndb.exceptionhandling.PgnDbException;
import com.engwork.pgndb.exceptionhandling.FailureType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IllegalMoveException  extends PgnDbException {
  private String message;
  @Override
  public String getHeaderMessage() {
    return "Illegal move!";
  }

  @Override
  public String getMainMessage() {
    return message;
  }

  @Override
  public FailureType getFailureType() {
    return FailureType.OperationFailure;
  }
}
