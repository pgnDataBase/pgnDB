package com.engwork.pgndb.appconfig.securityconfig;

import com.engwork.pgndb.exceptionhandling.OperationException;

class SecurityConfigException extends OperationException {
  SecurityConfigException(Throwable cause) {
    super("Invalid security configuration!", cause.getMessage());
  }
}