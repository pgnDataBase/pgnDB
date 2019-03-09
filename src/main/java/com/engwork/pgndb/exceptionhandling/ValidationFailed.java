package com.engwork.pgndb.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ValidationFailed {
  private String objectName;
  private String field;
  private List<String> reasons;
}

