package com.engwork.pgndb.statusresolver.exceptions;

import com.engwork.pgndb.exceptionhandling.OperationException;

public class StatusByKeyNotFoundException extends OperationException {
  public StatusByKeyNotFoundException(String key) {
    super("Status update by key", "Status not found by key [" + key + "]!");
  }
}
