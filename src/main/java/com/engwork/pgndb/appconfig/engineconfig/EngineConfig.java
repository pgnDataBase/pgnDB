package com.engwork.pgndb.appconfig.engineconfig;

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
public class EngineConfig {
  private String engineUrl;
  private String login;
  private String password;

  private static final String PASSWORD = "password";
  private static final String LOGIN = "login";
  private static final String ENGINE_URL = "engineUrl";

  static EngineConfig fromJson(JSONObject jsonObject) {
    try {
      EngineConfigBuilder engineConfigBuilder = new EngineConfigBuilder();
      engineConfigBuilder.engineUrl = jsonObject.getString(ENGINE_URL);
      engineConfigBuilder.login = jsonObject.getString(LOGIN);
      engineConfigBuilder.password = jsonObject.getString(PASSWORD);
      return engineConfigBuilder.build();
    } catch (Exception exception) {
      log.error("Invalid engine configuration!");
      throw new EngineConfigException(exception);
    }
  }
}
