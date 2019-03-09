package com.engwork.pgndb.appconfig.securityconfig;

import com.engwork.pgndb.appconfig.JsonFileReaderInjector;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JsonFileReaderInjector.class})
public class SecurityConfigTest {

  private static final String RESOURCES_PATH = "src/test/resources/".replace("/", File.separator);
  private final static String SECURITY = "security";

  @Test
  public void testValidSecurityConfig() throws IOException {
    // given
    String fileName = RESOURCES_PATH + "valid_test_config.json";

    String encryptionSecret = "awesrtytujghgfdsew3";
    String jwtSecret = "dxxsfdeqwrthyt54";
    int jwtExpirationMinutes = 240;
    List<String> cors = Collections.singletonList("http://172.19.0.2:4200");

    // when
    JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));
    SecurityConfig securityConfig = SecurityConfig.fromJson(jsonObject.getJSONObject(SECURITY));

    // then
    Assertions.assertEquals(encryptionSecret, securityConfig.getEncryptionSecret());
    Assertions.assertEquals(jwtSecret, securityConfig.getJwtSecret());
    Assertions.assertEquals(jwtExpirationMinutes, securityConfig.getJwtExpirationMinutes());

    List<String> actualCors = securityConfig.getCors();
    actualCors.removeAll(cors);
    Assertions.assertTrue(actualCors.isEmpty());
  }

  @Test
  public void testInvalidSecurityConfig() {
    // given
    String fileName = RESOURCES_PATH + "invalid_test_config.json";

    // then
    Assertions.assertThrows(
        SecurityConfigException.class,
        // when
        () -> {
          JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));
          SecurityConfig.fromJson(jsonObject.getJSONObject(SECURITY));
        }
    );
  }

}
