package com.engwork.pgndb.statusresolver;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
class StatusResolverInjector {

  private static final String LOADING_STATUS_RESOLVER = "loading.status.resolver";
  private static final String TREE_BUILDING_STATUS_RESOLVER = "tree.building.status.resolver";

  @Bean(name = LOADING_STATUS_RESOLVER)
  StatusResolver getLoadingStatusResolver() {
    return new LoadingStatusResolver();
  }

  @Bean(name = TREE_BUILDING_STATUS_RESOLVER)
  StatusResolver getTreeBuildingStatusResolver() {
    return new TreeBuildingStatusResolver();
  }

}
