package com.engwork.pgndb.accessauthorization;

import com.engwork.pgndb.databasesharing.DatabaseSharingBean;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class SharingAuthorizationInjector {

  private final DatabaseSharingBean databaseSharingBean;

  @Bean
  SharingAuthorizationBean getSharingAuthorizationBean() {
    return new SharingAuthorizationBeanImpl(databaseSharingBean);
  }
}
