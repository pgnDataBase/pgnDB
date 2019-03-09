package com.engwork.pgndb.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnknownReasonException extends PgnDbException {

  private String mainMessage;

  @Override
  public String getHeaderMessage() {
    return "Unknown failure";
  }

  @Override
  public String getMainMessage() {
    return this.mainMessage;
  }

  @Override
  public FailureType getFailureType() {
    return FailureType.Unknown;
  }
}
