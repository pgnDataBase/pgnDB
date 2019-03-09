package com.engwork.pgndb.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public abstract class PgnDbException extends RuntimeException implements PgnDbExceptionModel {
  protected FailureType failureType;
}
