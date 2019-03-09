package com.engwork.pgndb.accessauthorization;

import com.engwork.pgndb.exceptionhandling.ValidationException;
import java.util.UUID;

public interface AccessExceptionsFactory {

  ValidationException noAccessWithId(UUID id);

  ValidationException noAccessWithName(String name);

  ValidationException notOwnerWithId(UUID id);

  ValidationException notOwnerWithName(String name);
}
