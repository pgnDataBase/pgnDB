package com.engwork.pgndb.appconfig.engineconfig;

import com.engwork.pgndb.appconfig.JsonFileReader;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class EngineConfigInjector {

  private JsonFileReader jsonFileReader;

  private final static String ENGINE = "engine";
  private static final String CONFIG_FILE_NAME = "config.json";

  @Bean
  EngineConfig getEngineConfig() {
    JSONObject jsonConfig = jsonFileReader.read(CONFIG_FILE_NAME);
    return EngineConfig.fromJson(jsonConfig.getJSONObject(ENGINE));
  }
}
