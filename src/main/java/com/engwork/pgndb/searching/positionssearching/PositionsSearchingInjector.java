package com.engwork.pgndb.searching.positionssearching;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
class PositionsSearchingInjector {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  public PositionsSearchingInjector(DatabaseContainer databaseContainer,
                                    @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Bean
  PositionsFilterValidatorBean getPositionsFilterValidatorBean() {
    return new PositionsFilterValidatorBeanImpl();
  }

  @Bean
  PositionsSearchingBean getPositionsSearchingBean() {
    return new PositionsSearchingBeanImpl(sqlSessionFactory, databaseContainer, getPositionsFilterValidatorBean());
  }
}
