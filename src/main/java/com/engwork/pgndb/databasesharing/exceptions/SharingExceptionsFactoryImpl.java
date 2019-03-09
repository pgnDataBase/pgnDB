package com.engwork.pgndb.databasesharing.exceptions;

import com.engwork.pgndb.exceptionhandling.ValidationException;
import java.util.UUID;

public class SharingExceptionsFactoryImpl implements SharingExceptionsFactory {

  @Override
  public ValidationException cannotShareWithYourself(UUID id) {
    return generateWithReason("You are owner of database with id " + id.toString() + ". You cannot share it with yourself!");
  }

  @Override
  public ValidationException userNotExistsWithUsername(String username) {
    return generateWithReason("User with username " + username + " doesn't exist!");
  }


  private ValidationException generateWithReason(String reason) {
    return ValidationException.forSingleObjectAndFieldAndReason(
        "Database sharing",
        "Database sharing validation",
        reason
    );
  }
}
