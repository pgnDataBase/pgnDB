package com.engwork.pgndb.appconfig.engineconfig;

import com.engwork.pgndb.exceptionhandling.OperationException;

class EngineConfigException extends OperationException {
  EngineConfigException(Throwable cause) {
    super("Invalid engine configuration!", cause.getMessage());
  }
}