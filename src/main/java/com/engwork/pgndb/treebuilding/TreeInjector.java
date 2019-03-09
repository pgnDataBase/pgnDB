package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.statusresolver.StatusResolver;
import com.engwork.pgndb.treebuilding.responsemodel.TreeNodeResponseFactory;
import com.engwork.pgndb.treebuilding.treenodescalculator.TreeNodesCalculatorBean;
import com.engwork.pgndb.treebuilding.treenodesmerging.TreeNodesMergingBean;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatusBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TreeInjector {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
  private final TreeNodesCalculatorBean treeNodesCalculatorBean;
  private final StatusResolver statusResolver;
  private final TreeNodeResponseFactory treeNodeResponseFactory;
  private final TreeStatusBean treeStatusBean;
  private final TreeNodesMergingBean treeNodesMergingBean;

  @Autowired
  public TreeInjector(DatabaseContainer databaseContainer,
                      @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory,
                      TreeNodesCalculatorBean treeNodesCalculatorBean,
                      @Qualifier("tree.building.status.resolver") StatusResolver statusResolver,
                      TreeNodeResponseFactory treeNodeResponseFactory,
                      TreeStatusBean treeStatusBean,
                      TreeNodesMergingBean treeNodesMergingBean) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
    this.treeNodesCalculatorBean = treeNodesCalculatorBean;
    this.statusResolver = statusResolver;
    this.treeNodeResponseFactory = treeNodeResponseFactory;
    this.treeStatusBean = treeStatusBean;
    this.treeNodesMergingBean = treeNodesMergingBean;
  }

  @Bean
  MovesToTreeLoadingBean getMovesToTreeLoadingBean() {
    return new MovesToTreeLoadingBeanImpl(sqlSessionFactory, treeNodesCalculatorBean, treeNodesMergingBean);
  }

  @Bean
  public TreeBuildingBean treeBuildingBean() {
    return new TreeBuildingBeanImpl(databaseContainer, sqlSessionFactory, getMovesToTreeLoadingBean(), treeStatusBean, statusResolver);
  }

  @Bean
  public TreeSearchingResultMappingBean getTreeSearchingResultMappingBean() {
    return new TreeSearchingResultMappingBeanImpl(databaseContainer, sqlSessionFactory, treeNodeResponseFactory);
  }

  @Bean
  public TreeSearchingBean treeSearchingBean() {
    return new TreeSearchingBeanImpl(databaseContainer, sqlSessionFactory, getTreeSearchingResultMappingBean(), treeStatusBean);
  }
}
