package com.engwork.pgndb.appconfig;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonFileReaderInjector {

  @Bean
  public JsonFileReader getJsonFileReader() {
    return new JsonFileReaderImpl();
  }
}
