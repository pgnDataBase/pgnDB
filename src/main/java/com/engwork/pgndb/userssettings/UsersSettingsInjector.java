package com.engwork.pgndb.userssettings;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UsersSettingsInjector {

  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  public UsersSettingsInjector(@Qualifier("default.session.factory") SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Bean
  UsersSettingsDaoMapperBean getUsersSettingsDaoMapperBean() {
    return new UsersSettingsDaoMapperBeanImpl();
  }

  @Bean
  UsersSettingsBean getUsersSettingsBean() {
    return new UsersSettingsBeanImpl(this.sqlSessionFactory, getUsersSettingsDaoMapperBean());
  }
}
