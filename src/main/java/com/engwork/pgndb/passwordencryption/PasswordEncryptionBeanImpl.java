package com.engwork.pgndb.passwordencryption;

import com.engwork.pgndb.appconfig.securityconfig.SecurityConfig;
import com.engwork.pgndb.passwordencryption.exceptions.DecryptionFailedException;
import com.engwork.pgndb.passwordencryption.exceptions.EncryptionFailedException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
class PasswordEncryptionBeanImpl implements PasswordEncryptionBean {

  private final SecureRandom secureRandom = new SecureRandom();
  private final SecretKeySpec secretKeySpec;

  PasswordEncryptionBeanImpl(SecurityConfig securityConfig) throws NoSuchAlgorithmException {
    String secret = securityConfig.getEncryptionSecret();
    byte[] key = secret.getBytes(StandardCharsets.UTF_8);
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16);
    secretKeySpec = new SecretKeySpec(key, "AES");
  }

  @Override
  public EncryptedPassword encrypt(String plain) {
    byte[] iv = new byte[128 / 8];
    secureRandom.nextBytes(iv);
    byte[] encryptedBytes = process(Cipher.ENCRYPT_MODE, plain.getBytes(StandardCharsets.UTF_8), iv);
    return new EncryptedPassword(encryptedBytes, iv);
  }

  @Override
  public String decrypt(EncryptedPassword encryptedPassword) {
    byte[] decryptedBytes = process(Cipher.DECRYPT_MODE, encryptedPassword.getPassword(), encryptedPassword.getSalt());
    return new String(decryptedBytes, StandardCharsets.UTF_8);
  }

  private byte[] process(int mode, byte[] plainBytes, byte[] iv) {
    try {
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(mode, secretKeySpec, ivParameterSpec);

      return cipher.doFinal(plainBytes);
    } catch (Exception exception) {
      if (mode == Cipher.ENCRYPT_MODE) {
        throw new EncryptionFailedException(exception);
      } else {
        throw new DecryptionFailedException(exception);
      }
    }
  }
}
