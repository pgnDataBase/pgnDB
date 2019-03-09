package com.engwork.pgndb.databasemanager.exceptions;

import com.engwork.pgndb.exceptionhandling.OperationException;
import java.util.UUID;

public class UserDoesNotExistException extends OperationException {
  public UserDoesNotExistException(String operation, UUID userId) {
    super(operation, "User with id " + userId.toString() + " doesn't exist");
  }
  public UserDoesNotExistException(String operation, String username) {
    super(operation, "User with username " + username + " doesn't exist");
  }
}
