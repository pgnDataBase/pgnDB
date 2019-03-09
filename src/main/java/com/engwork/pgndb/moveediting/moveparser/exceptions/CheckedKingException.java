package com.engwork.pgndb.moveediting.moveparser.exceptions;

import com.engwork.pgndb.exceptionhandling.PgnDbException;
import com.engwork.pgndb.exceptionhandling.FailureType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CheckedKingException extends PgnDbException {

  private String color;

  @Override
  public String getHeaderMessage() {
    return "Illegal move!";
  }

  @Override
  public String getMainMessage() {
    return "You can't make, because "+color.toLowerCase()+" King is checked!";
  }

  @Override
  public FailureType getFailureType() {
    return FailureType.OperationFailure;
  }
}
