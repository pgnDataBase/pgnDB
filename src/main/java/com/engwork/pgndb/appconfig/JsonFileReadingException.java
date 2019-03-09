package com.engwork.pgndb.appconfig;

import com.engwork.pgndb.exceptionhandling.OperationException;

class JsonFileReadingException extends OperationException {
  JsonFileReadingException(Throwable cause) {
    super("Invalid configuration file!", cause.getMessage());
  }
}
