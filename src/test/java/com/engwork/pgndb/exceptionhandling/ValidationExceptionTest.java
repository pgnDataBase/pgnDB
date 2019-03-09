package com.engwork.pgndb.exceptionhandling;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ValidationExceptionTest {

  @Test
  public void shouldReturnProperMessage() {
    // given
    List<ValidationFailed> failedValidations = new ArrayList<>();

    List<String> reasonsFirstValidationFailed = new ArrayList<>();
    reasonsFirstValidationFailed.add("Username too long.");
    reasonsFirstValidationFailed.add("Username contains forbidden characters.");
    ValidationFailed firstFailed = new ValidationFailed("user", "username", reasonsFirstValidationFailed);
    failedValidations.add(firstFailed);

    List<String> reasonsSecondValidationFailed = Collections.singletonList("Password too weak.");
    ValidationFailed secondFailed = new ValidationFailed("user", "password", reasonsSecondValidationFailed);
    failedValidations.add(secondFailed);

    // when
    ValidationException validationException = new ValidationException(failedValidations);

    // then
    String expectedMessage =
        "Validation failed for: user\n" +
            "Field failed: username\n" +
            "Reason: Username too long.\n" +
            "Reason: Username contains forbidden characters.\n" +
            "Validation failed for: user\n" +
            "Field failed: password\n" +
            "Reason: Password too weak.\n";
    assertThat(validationException.getMainMessage()).isEqualTo(expectedMessage);
  }
}