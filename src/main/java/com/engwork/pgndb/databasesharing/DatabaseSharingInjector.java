package com.engwork.pgndb.databasesharing;

import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.accessauthorization.OwnershipAuthorizationBean;
import com.engwork.pgndb.databasesharing.exceptions.SharingExceptionsFactory;
import com.engwork.pgndb.users.UsersBean;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DatabaseSharingInjector {

  private final SqlSessionFactory sqlSessionFactory;
  private final UsersBean usersBean;
  private final OwnershipAuthorizationBean ownershipAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;
  private final SharingExceptionsFactory sharingExceptionsFactory;

  @Bean
  DatabaseSharingBean getDatabaseSharingBean() {
    return new DatabaseSharingBeanImpl(sqlSessionFactory, usersBean, ownershipAuthorizationBean, sharingExceptionsFactory, accessExceptionsFactory);
  }
}
