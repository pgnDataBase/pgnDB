package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.appconfig.databaseconfig.DatabaseConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class DynamicDataSourceBuilderInjector {

  private final DatabaseConfig databaseConfig;

  @Bean
  DynamicDataSourceBuilder getDynamicDataSourceBuilder() {
    return new DynamicDataSourceBuilderImpl(databaseConfig);
  }
}
