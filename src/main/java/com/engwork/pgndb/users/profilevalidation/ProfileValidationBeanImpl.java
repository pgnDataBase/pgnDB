package com.engwork.pgndb.users.profilevalidation;

import com.engwork.pgndb.exceptionhandling.ValidationException;
import static java.lang.String.format;

class ProfileValidationBeanImpl implements ProfileValidationBean {

  private static final int USERNAME_MIN_LENGTH = 6;
  private static final int USERNAME_MAX_LENGTH = 20;
  private static final int PASSWORD_MIN_LENGTH = 6;
  private static final int PASSWORD_MAX_LENGTH = 50;

  @Override
  public void validateUsername(String username) {
    if (username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH) {
      throw ValidationException.forSingleObjectAndFieldAndReason(
          "Profile Validation",
          "username",
          format("Username must be between %d and %d signs!", USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH)
      );
    }
  }

  @Override
  public void validatePassword(String password) {
    if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
      throw ValidationException.forSingleObjectAndFieldAndReason(
          "Profile Validation",
          "password",
          format("Password must be between %d and %d signs!", PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH)
      );
    }
  }
}
