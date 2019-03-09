package com.engwork.pgndb.users.profilevalidation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProfileValidationInjector {

  @Bean
  ProfileValidationBean getProfileValidationBean() {
    return new ProfileValidationBeanImpl();
  }

}
