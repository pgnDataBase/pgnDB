package com.engwork.pgndb.appconfig.databaseconfig;

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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JsonFileReaderInjector.class})
public class DatabaseConfigTest {

  private static final String RESOURCES_PATH = "src/test/resources/".replace("/", File.separator);
  private final static String DATABASE = "database";

  @Test
  public void testValidDatabaseConfig() throws IOException {
    // given
    String fileName = RESOURCES_PATH + "valid_test_config.json";

    String driver = "test.Driver";
    String url = "test-url:9909";
    String username = "test-username";
    String password = "test-password";
    Integer maximumPoolSize = 2;
    Integer connectionTimeout = 300;

    // when
    JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));

    // then
    DatabaseConfig databaseConfig = DatabaseConfig.fromJson(jsonObject.getJSONObject(DATABASE));

    Assertions.assertEquals(driver, databaseConfig.getDriver());
    Assertions.assertEquals(url, databaseConfig.getUrl());
    Assertions.assertEquals(username, databaseConfig.getUsername());
    Assertions.assertEquals(password, databaseConfig.getPassword());
    Assertions.assertEquals(maximumPoolSize, databaseConfig.getMaximumPoolSize());
    Assertions.assertEquals(connectionTimeout, databaseConfig.getConnectionTimeout());

  }

  @Test
  public void testInvalidDatabaseConfig() {
    // given
    String fileName = RESOURCES_PATH + "invalid_test_config.json";

    // then
    Assertions.assertThrows(
        DatabaseConfigException.class,
        // when
        () -> {
          JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));
          DatabaseConfig.fromJson(jsonObject.getJSONObject(DATABASE));
        }
    );
  }

}
