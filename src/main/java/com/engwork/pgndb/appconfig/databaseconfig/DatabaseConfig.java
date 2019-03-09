package com.engwork.pgndb.appconfig.databaseconfig;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseConfig {
  private String driver;
  private String url;
  private String username;
  private String password;
  private Integer maximumPoolSize;
  private Integer connectionTimeout;

  private static final String DRIVER = "driver";
  private static final String URL = "url";
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String MAXIMUM_CONNECTION_POOL = "maximumPoolSize";
  private static final String CONNECTION_TIMEOUT = "connectionTimeout";

  static DatabaseConfig fromJson(JSONObject jsonObject) {
    try {
      DatabaseConfigBuilder configBuilder = new DatabaseConfigBuilder();
      configBuilder.driver = jsonObject.getString(DRIVER);
      configBuilder.url = jsonObject.getString(URL);
      configBuilder.username = jsonObject.getString(USERNAME);
      configBuilder.password = jsonObject.getString(PASSWORD);
      configBuilder.maximumPoolSize = jsonObject.getInt(MAXIMUM_CONNECTION_POOL);
      configBuilder.connectionTimeout = jsonObject.getInt(CONNECTION_TIMEOUT);
      return configBuilder.build();
    } catch (Exception exception) {
      log.error("Invalid database configuration!");
      throw new DatabaseConfigException(exception);
    }
  }
}
