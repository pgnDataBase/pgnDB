package com.engwork.pgndb.databasesharing.exceptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharingExceptionsFactoryInjector {
  @Bean
  SharingExceptionsFactory getSharingExceptionsFactory(){
    return new SharingExceptionsFactoryImpl();
  }
}
