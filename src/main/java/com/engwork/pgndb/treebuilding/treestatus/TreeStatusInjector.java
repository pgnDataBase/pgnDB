package com.engwork.pgndb.treebuilding.treestatus;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TreeStatusInjector {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  public TreeStatusInjector(DatabaseContainer databaseContainer,
                            @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Bean
  TreeStatusBean getTreeStatusBean() {
    return new TreeStatusBeanImpl(databaseContainer, sqlSessionFactory);
  }

}
