package com.engwork.pgndb.chessdatabases.databasevalidation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DatabaseValidationInjector {

  @Bean
  DatabaseValidationBean getDatabaseValidationBean(){
    return new DatabaseValidationBeanImpl();
  }
}
