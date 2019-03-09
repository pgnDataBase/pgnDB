package com.engwork.pgndb.chessgamesdownloader;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GameMetadataExtracterInjector {

  @Autowired
  GameMetadataExtracterInjector(DatabaseContainer databaseContainer,
                                @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Bean
  GameMetadataExtracter getGameMetadataExtracter() {
    return new GameMetadataExtracterImpl(sqlSessionFactory, databaseContainer);
  }

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
}
