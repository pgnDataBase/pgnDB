package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
class LoadersInjector {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  public LoadersInjector(DatabaseContainer databaseContainer,
                         @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Bean
  EventsLoaderBean getEventsLoaderBean() {
    return new EventsLoaderBeanImpl(this.sqlSessionFactory, this.databaseContainer);
  }

  @Bean
  PlayersLoaderBean getPlayersLoaderBean() {
    return new PlayersLoaderBeanImpl(this.sqlSessionFactory, this.databaseContainer);
  }

  @Bean
  MetadataLoaderBean getMetadataLoaderBean() {
    return new MetadataLoaderBeanImpl(this.sqlSessionFactory, this.databaseContainer);
  }

  @Bean
  PositionsLoaderBean getPositionsLoaderBean() {
    return new PositionsLoaderBeanImpl(this.sqlSessionFactory, this.databaseContainer);
  }

  @Bean
  MovesLoaderBean getMovesLoaderBean() {
    return new MovesLoaderBeanImpl(this.sqlSessionFactory, this.databaseContainer);
  }

  @Bean
  TagsLoaderBean getTagsLoaderBean() {
    return new TagsLoaderBeanImpl(this.sqlSessionFactory, this.databaseContainer);
  }

}
