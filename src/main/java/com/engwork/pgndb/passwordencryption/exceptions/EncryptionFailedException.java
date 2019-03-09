package com.engwork.pgndb.passwordencryption.exceptions;

public class EncryptionFailedException extends RuntimeException {
  public EncryptionFailedException(Exception cause) {
    super("Error during encryption!", cause);
  }
}
