package com.engwork.pgndb.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@AllArgsConstructor
public class ValidationException extends PgnDbException {

  private List<ValidationFailed> validationsFailed;

  public static ValidationException forSingleObjectAndFieldAndReason(String objectName, String fieldName, String reason) {
    List<ValidationFailed> validationsFailed = new ArrayList<>();
    validationsFailed.add(new ValidationFailed(objectName, fieldName, Collections.singletonList(reason)));
    return new ValidationException(validationsFailed);
  }

  @Override
  public String getHeaderMessage() {
    return "Validation failed";
  }

  @Override
  public String getMainMessage() {
    StringBuilder result = new StringBuilder();
    if (this.validationsFailed != null) {
      for (ValidationFailed validationFailed : this.validationsFailed) {
        this.appendForValidationFailureHelper(result, validationFailed);
      }
    }
    return result.toString();
  }

  private void appendForValidationFailureHelper(StringBuilder result, ValidationFailed validationFailed) {
    result.append("Validation failed for: ");
    result.append(validationFailed.getObjectName());
    result.append("\nField failed: ");
    result.append(validationFailed.getField());
    result.append("\n");
    if (validationFailed.getReasons() != null) {
      for (String reason : validationFailed.getReasons()) {
        result.append("Reason: ");
        result.append(reason);
        result.append("\n");
      }
    }
  }

  @Override
  public FailureType getFailureType() {
    return FailureType.ValidationFailure;
  }
}
