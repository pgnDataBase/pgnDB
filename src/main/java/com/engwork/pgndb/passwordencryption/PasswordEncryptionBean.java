package com.engwork.pgndb.passwordencryption;

public interface PasswordEncryptionBean {
  EncryptedPassword encrypt(String password);
  String decrypt(EncryptedPassword encryptedPassword);
}
