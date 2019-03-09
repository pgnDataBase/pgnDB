package com.engwork.pgndb.appconfig.engineconfig;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EngineConfigTest {

  private static final String RESOURCES_PATH = "src/test/resources/".replace("/", File.separator);
  private final static String ENGINE = "engine";

  @Test
  public void testValidEngineConfig() throws IOException {
    // given
    String fileName = RESOURCES_PATH + "valid_test_config.json";

    String engineUrl = "http://10.0.2.2:7080";
    String login = "test";
    String password = "111111";

    // when
    JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));
    EngineConfig engineConfig = EngineConfig.fromJson(jsonObject.getJSONObject(ENGINE));

    // then
    Assertions.assertEquals(engineUrl, engineConfig.getEngineUrl());
    Assertions.assertEquals(login, engineConfig.getLogin());
    Assertions.assertEquals(password, engineConfig.getPassword());
  }


  @Test
  public void testInvalidEngineConfig() {
    // given
    String fileName = RESOURCES_PATH + "invalid_test_config.json";

    // then
    Assertions.assertThrows(
        EngineConfigException.class,
        // when
        () -> {
          JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));
          EngineConfig.fromJson(jsonObject.getJSONObject(ENGINE));
        }
    );
  }

}
