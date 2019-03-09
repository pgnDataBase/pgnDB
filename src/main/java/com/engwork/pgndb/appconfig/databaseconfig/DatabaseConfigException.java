package com.engwork.pgndb.appconfig.databaseconfig;

import com.engwork.pgndb.exceptionhandling.OperationException;

class DatabaseConfigException extends OperationException {
  DatabaseConfigException(Throwable cause) {
    super("Invalid database configuration!", cause.getMessage());
  }
}