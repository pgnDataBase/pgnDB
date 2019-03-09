package com.engwork.pgndb.accessauthorization;

import com.engwork.pgndb.chessdatabases.ChessDatabaseSearchingBean;
import com.engwork.pgndb.chessdatabases.ChessDatabasesManagementBean;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class OwnershipAuthorizationInjector {

  private final ChessDatabasesManagementBean chessDatabasesManagementBean;
  private final ChessDatabaseSearchingBean chessDatabaseSearchingBean;

  @Bean
  OwnershipAuthorizationBean getOwnershipAuthorizationBean() {
    return new OwnershipAuthorizationBeanImpl(chessDatabaseSearchingBean, chessDatabasesManagementBean);
  }
}
