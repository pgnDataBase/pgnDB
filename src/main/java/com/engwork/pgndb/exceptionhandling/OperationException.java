package com.engwork.pgndb.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class OperationException extends PgnDbException {

  private String operationName;
  private String reason;

  @Override
  public String getHeaderMessage() {
    return this.operationName + " has failed.";
  }

  @Override
  public String getMainMessage() {
    return "Reason is: " + this.reason;
  }

  @Override
  public FailureType getFailureType() {
    return FailureType.OperationFailure;
  }
}
