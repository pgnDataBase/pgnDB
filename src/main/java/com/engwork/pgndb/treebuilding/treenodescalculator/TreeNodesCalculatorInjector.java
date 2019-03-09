package com.engwork.pgndb.treebuilding.treenodescalculator;

import com.engwork.pgndb.chessgamesconverter.PositionBuilder;
import com.engwork.pgndb.treebuilding.treenodesmerging.TreeNodesMergingBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TreeNodesCalculatorInjector {

  private final PositionBuilder positionBuilder;
  private final SqlSessionFactory sqlSessionFactory;
  private final TreeNodesMergingBean treeNodesMergingBean;

  @Autowired
  public TreeNodesCalculatorInjector(PositionBuilder positionBuilder,
                                     @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory,
                                     TreeNodesMergingBean treeNodesMergingBean) {
    this.positionBuilder = positionBuilder;
    this.sqlSessionFactory = sqlSessionFactory;
    this.treeNodesMergingBean = treeNodesMergingBean;
  }

  @Bean
  StartTreeNodeDeliveryBean getStartTreeNodeDeliveryBean() {
    return new StartTreeNodeDeliveryBeanImpl(sqlSessionFactory, positionBuilder);
  }

  @Bean
  PlainTreeNodesCalculatorBean getPlainTreeNodesCalculatorBean() {
    return new PlainTreeNodesCalculatorBeanImpl(getStartTreeNodeDeliveryBean());
  }

  @Bean
  VariantTreeNodesCalculatorBean getVariantTreeNodesCalculatorBean() {
    return new VariantTreeNodesCalculatorBeanImpl(getPlainTreeNodesCalculatorBean());
  }

  @Bean
  TreeNodesCalculatorBean getTreeNodesCalculatorBean() {
    return new TreeNodesCalculatorBeanImpl(treeNodesMergingBean, getPlainTreeNodesCalculatorBean(), getVariantTreeNodesCalculatorBean());
  }
}
