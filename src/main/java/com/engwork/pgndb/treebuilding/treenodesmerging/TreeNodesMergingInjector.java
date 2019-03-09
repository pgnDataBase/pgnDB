package com.engwork.pgndb.treebuilding.treenodesmerging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TreeNodesMergingInjector {

  @Bean
  TreeNodesMergingBean getTreeNodesMergingBean() {
    return new TreeNodesMergingBeanImpl();
  }
}
