package com.engwork.pgndb.databasemanager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SessionConfigProviderInjector {

  @Bean
  SessionConfigProvider getSessionConfigProvider() {
    return new SessionConfigProviderImpl();
  }

}
