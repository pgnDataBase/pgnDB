package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.chessdatabases.ChessDatabasesManagementBean;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
class PSQLManagerInjector {

  @Bean
  PSQLManager getPsqlManager() {
    return new PSQLManagerImpl(chessDatabasesManagementBean);
  }

  private final ChessDatabasesManagementBean chessDatabasesManagementBean;
}
