package com.engwork.pgndb.databasemanager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DatabaseContainerInjector {

  @Bean
  DatabaseContainer getDatabaseContainer() {
    return new DatabaseContainerImpl();
  }

}
