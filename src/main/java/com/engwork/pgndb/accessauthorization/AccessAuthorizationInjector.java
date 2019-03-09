package com.engwork.pgndb.accessauthorization;

import com.engwork.pgndb.chessdatabases.ChessDatabasesManagementBean;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class AccessAuthorizationInjector {

  private final ChessDatabasesManagementBean chessDatabasesManagementBean;
  private final SharingAuthorizationBean sharingAuthorizationBean;
  private final OwnershipAuthorizationBean ownershipAuthorizationBean;

  @Bean
  AccessAuthorizationBean getAccessAuthorizationBean() {
    return new AccessAuthorizationBeanImpl(ownershipAuthorizationBean, sharingAuthorizationBean, chessDatabasesManagementBean);
  }
}
