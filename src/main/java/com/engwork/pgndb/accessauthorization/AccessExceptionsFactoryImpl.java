package com.engwork.pgndb.accessauthorization;

import com.engwork.pgndb.exceptionhandling.ValidationException;
import static java.lang.String.format;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class AccessExceptionsFactoryImpl implements AccessExceptionsFactory {

  @Override
  public ValidationException noAccessWithId(UUID id) {
    return generateWithReason(format("You have no access to database with id: %s!", id.toString()));
  }

  @Override
  public ValidationException noAccessWithName(String name) {
    return generateWithReason(format("You have no access to database with name: %s!", name));
  }

  @Override
  public ValidationException notOwnerWithId(UUID id) {
    return generateWithReason(format("You are not owner of database with id: %s!", id.toString()));
  }

  @Override
  public ValidationException notOwnerWithName(String name) {
    return generateWithReason(format("You are not owner of database with name: %s!", name));
  }

  private ValidationException generateWithReason(String reason) {
    return ValidationException.forSingleObjectAndFieldAndReason(
        "Access Authorization",
        "Access Authorization",
        reason
    );
  }
}
