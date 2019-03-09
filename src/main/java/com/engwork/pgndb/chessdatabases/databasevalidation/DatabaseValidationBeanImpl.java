package com.engwork.pgndb.chessdatabases.databasevalidation;

import com.engwork.pgndb.exceptionhandling.ValidationException;
import static java.lang.String.format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DatabaseValidationBeanImpl implements DatabaseValidationBean {

  private static final int DATABASE_NAME_MIN_LENGTH = 1;
  private static final int DATABASE_NAME_MAX_LENGTH = 20;

  @Override
  public void validateName(String databaseName) {
    if (databaseName.length() < DATABASE_NAME_MIN_LENGTH || databaseName.length() > DATABASE_NAME_MAX_LENGTH) {
      throw ValidationException.forSingleObjectAndFieldAndReason(
          "Database Validation",
          "databaseName",
          format("Chess database name must be between %d and %d signs!", DATABASE_NAME_MIN_LENGTH, DATABASE_NAME_MAX_LENGTH)
      );
    }

    List<Character> illegalCharacters = findIllegalCharacters(databaseName);
    if (!illegalCharacters.isEmpty()) {
      throw ValidationException.forSingleObjectAndFieldAndReason(
          "Database Validation",
          "databaseName",
          format("Chess database name contains illegal characters: %s!", Arrays.toString(illegalCharacters.toArray()))
      );
    }
  }

  private List<Character> findIllegalCharacters(String databaseName) {
    List<Character> result = new ArrayList<>();
    char[] charArray = databaseName.toCharArray();
    for (char character : charArray) {
      if (!(Character.isLetterOrDigit(character) || character == ' ' || character == '-')) {
        result.add(character);
      }
    }
    return result;
  }
}
