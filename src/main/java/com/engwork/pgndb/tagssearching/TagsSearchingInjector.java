package com.engwork.pgndb.tagssearching;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
class TagsSearchingInjector {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  TagsSearchingInjector(DatabaseContainer databaseContainer,
                        @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Bean
  TagsSearchingBean getTagsSearchingBean() {
    return new TagsSearchingBeanImpl(sqlSessionFactory, databaseContainer, getSortWithStartValueBean());
  }

  @Bean
  SortWithStartValueBean getSortWithStartValueBean() {
    return new SortWithStartValueBeanImpl();
  }
}
