package com.engwork.pgndb.users.platformaccess;

import com.engwork.pgndb.databasemanager.DatabaseManager;
import com.engwork.pgndb.databasesharing.DatabaseSharingBean;
import com.engwork.pgndb.passwordencryption.PasswordEncryptionBean;
import com.engwork.pgndb.users.UsersBean;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RemoveAccountInjector {

  private final SqlSessionFactory sqlSessionFactory;
  private final PasswordEncryptionBean passwordEncryptionBean;
  private final DatabaseManager databaseManager;
  private final DatabaseSharingBean databaseSharingBean;
  private final UsersBean usersBean;

  @Bean
  RemoveAccountBean getRemoveAccountBean() {
    return new RemoveAccountBeanImpl(sqlSessionFactory, passwordEncryptionBean, databaseManager, databaseSharingBean, usersBean);
  }
}
