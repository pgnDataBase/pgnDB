package com.engwork.pgndb.users;

import com.engwork.pgndb.tagssearching.SortWithStartValueBean;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class UsersInjector {

  private final SqlSessionFactory sqlSessionFactory;
  private final SortWithStartValueBean sortWithStartValueBean;

  @Bean
  UsersBean usersBean() {
    return new UsersBeanImpl(sqlSessionFactory, sortWithStartValueBean);
  }
}
