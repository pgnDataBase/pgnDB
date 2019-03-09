package com.engwork.pgndb.databasesharing.exceptions;

import com.engwork.pgndb.exceptionhandling.ValidationException;
import java.util.UUID;

public interface SharingExceptionsFactory {

  ValidationException cannotShareWithYourself(UUID id);

  ValidationException userNotExistsWithUsername(String name);

}
