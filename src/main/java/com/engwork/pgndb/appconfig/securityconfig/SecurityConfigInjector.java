package com.engwork.pgndb.appconfig.securityconfig;

import com.engwork.pgndb.appconfig.JsonFileReader;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SecurityConfigInjector {

  private JsonFileReader jsonFileReader;

  private final static String SECURITY = "security";
  private static final String CONFIG_FILE_NAME = "config.json";

  @Bean
  SecurityConfig getSecurityConfig() {
    JSONObject jsonConfig = jsonFileReader.read(CONFIG_FILE_NAME);
    return SecurityConfig.fromJson(jsonConfig.getJSONObject(SECURITY));
  }
}
