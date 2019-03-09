package com.engwork.pgndb.exceptionhandling;

public interface PgnDbExceptionModel {
  String getHeaderMessage();

  String getMainMessage();

  FailureType getFailureType();
}
