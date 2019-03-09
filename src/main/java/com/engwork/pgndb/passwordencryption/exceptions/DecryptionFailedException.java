package com.engwork.pgndb.passwordencryption.exceptions;

public class DecryptionFailedException extends RuntimeException {
  public DecryptionFailedException(Exception cause) {
    super("Error during decryption!", cause);
  }
}
