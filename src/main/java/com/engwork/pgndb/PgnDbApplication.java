package com.engwork.pgndb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PgnDbApplication {
  public static void main(String[] args) {
    SpringApplication.run(PgnDbApplication.class, args);
  }
}

