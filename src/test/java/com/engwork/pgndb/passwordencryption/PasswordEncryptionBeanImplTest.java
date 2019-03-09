package com.engwork.pgndb.passwordencryption;

import com.engwork.pgndb.appconfig.JsonFileReaderInjector;
import com.engwork.pgndb.appconfig.securityconfig.SecurityConfigInjector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PasswordEncryptionInjector.class, JsonFileReaderInjector.class, SecurityConfigInjector.class})
class PasswordEncryptionBeanImplTest {

  @Autowired
  PasswordEncryptionBean passwordEncryptionBean;

  @Test
  void testEncryption() {
    // given
    String expected = "test-password-1";

    // when
    EncryptedPassword encryptedPassword = passwordEncryptionBean.encrypt(expected);

    String actual = passwordEncryptionBean.decrypt(encryptedPassword);

    // then
    Assertions.assertEquals(expected, actual);
  }
}