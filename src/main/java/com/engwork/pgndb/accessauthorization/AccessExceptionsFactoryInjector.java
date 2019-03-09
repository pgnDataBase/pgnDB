package com.engwork.pgndb.accessauthorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AccessExceptionsFactoryInjector {
  @Bean
  AccessExceptionsFactory getAccessExceptionsFactory(){
    return new AccessExceptionsFactoryImpl();
  }
}
