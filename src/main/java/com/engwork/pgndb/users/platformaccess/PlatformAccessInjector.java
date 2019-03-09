package com.engwork.pgndb.users.platformaccess;

import com.engwork.pgndb.passwordencryption.PasswordEncryptionBean;
import com.engwork.pgndb.security.JWTGenerator;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.users.UsersBean;
import com.engwork.pgndb.users.profilevalidation.ProfileValidationBean;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class PlatformAccessInjector {

  private final SqlSessionFactory sqlSessionFactory;
  private final JWTGenerator jwtGenerator;
  private final JWTValidator jwtValidator;
  private final UsersBean usersBean;
  private final PasswordEncryptionBean passwordEncryptionBean;
  private final ProfileValidationBean profileValidationBean;

  @Bean
  PlatformAccessBean getPlatformAccessBean() {
    return new PlatformAccessBeanImpl(sqlSessionFactory,
                                      jwtGenerator,
                                      jwtValidator,
                                      usersBean,
                                      passwordEncryptionBean,
                                      profileValidationBean
    );
  }
}
