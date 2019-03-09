package com.engwork.pgndb.appconfig.databaseconfig;

import com.engwork.pgndb.appconfig.JsonFileReader;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DatabaseConfigInjector {

  private JsonFileReader jsonFileReader;

  private final static String DATABASE = "database";
  private static final String CONFIG_FILE_NAME = "config.json";

  @Bean
  DatabaseConfig getDatabaseConfig() {
    JSONObject jsonConfig = jsonFileReader.read(CONFIG_FILE_NAME);
    return DatabaseConfig.fromJson(jsonConfig.getJSONObject(DATABASE));
  }
}
