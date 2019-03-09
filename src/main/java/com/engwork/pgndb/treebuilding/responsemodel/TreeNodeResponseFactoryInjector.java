package com.engwork.pgndb.treebuilding.responsemodel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TreeNodeResponseFactoryInjector {

  @Bean
  TreeNodeResponseFactory getTreeNodeResponseFactory() {
    return new TreeNodeResponseFactoryImpl();
  }

}
