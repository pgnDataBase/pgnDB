package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.searching.IntersectionResolverBean;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
class HeaderFieldSearchingInjector {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
  private final IntersectionResolverBean<ChessGameMetadataDAO> intersectionResolverBean;

  @Autowired
  public HeaderFieldSearchingInjector(DatabaseContainer databaseContainer,
                                      @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory,
                                      IntersectionResolverBean<ChessGameMetadataDAO> intersectionResolverBean) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
    this.intersectionResolverBean = intersectionResolverBean;
  }

  @Bean
  EventSearchingBean getEventSearchingBean() {
    return new EventSearchingBeanImpl(sqlSessionFactory, databaseContainer);
  }

  @Bean
  WhitePlayerSearchingBean getWhitePlayerSearchingBean() {
    return new WhitePlayerSearchingBeanImpl(sqlSessionFactory, databaseContainer);
  }

  @Bean
  BlackPlayerSearchingBean getBlackPlayerSearchingBean() {
    return new BlackPlayerSearchingBeanImpl(sqlSessionFactory, databaseContainer);
  }

  @Bean
  ResultSearchingBean getResultSearchingBean() {
    return new ResultSearchingBeanImpl(sqlSessionFactory, databaseContainer);
  }

  @Bean
  RoundSearchingBean getRoundSearchingBean() {
    return new RoundSearchingBeanImpl(sqlSessionFactory, databaseContainer);
  }

  @Bean
  DateSearchingBean getDateSearchingBean() {
    return new DateSearchingBeanImpl(sqlSessionFactory, databaseContainer);
  }

  @Bean
  AdditionalSearchingBean getAdditionalSearchingBean() {
    return new AdditionalSearchingBeanImpl(sqlSessionFactory, databaseContainer, intersectionResolverBean);
  }
}
