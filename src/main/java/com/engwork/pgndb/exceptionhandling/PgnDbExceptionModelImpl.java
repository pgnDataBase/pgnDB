package com.engwork.pgndb.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is class that resembles data that actually goes to client when our
 * pgndb exceptions occurs
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
class PgnDbExceptionModelImpl implements PgnDbExceptionModel {
  private String headerMessage;
  private String mainMessage;
  private FailureType failureType;
}
