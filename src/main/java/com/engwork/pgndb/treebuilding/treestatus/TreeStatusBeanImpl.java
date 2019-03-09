package com.engwork.pgndb.treebuilding.treestatus;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.treebuilding.mapper.TreeStatusMapper;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class TreeStatusBeanImpl implements TreeStatusBean {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;

  @Override
  public TreeStatus getTreeStatus(String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      TreeStatusMapper treeStatusMapper = sqlSession.getMapper(TreeStatusMapper.class);
      return treeStatusMapper.selectTreeStatus();
    }
  }

  @Override
  public void updateTreeStatus(String databaseName, TreeStatus treeStatus) {
    databaseContainer.setDatabaseKey(databaseName);
    try (SqlSession session = sqlSessionFactory.openSession()) {
      session.getMapper(TreeStatusMapper.class).insertTreeStatus(treeStatus);
      session.commit();
    }
  }

  @Override
  public void setTreeNotUpToDate(String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    try (SqlSession session = sqlSessionFactory.openSession()) {
      session.getMapper(TreeStatusMapper.class).updateUpToDateFlag(false);
      session.commit();
    }
  }

  @Override
  public Boolean doesTreeExist(String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      TreeStatusMapper treeStatusMapper = sqlSession.getMapper(TreeStatusMapper.class);
      return treeStatusMapper.countTreeStatuses() > 0;
    }
  }

  @Override
  public void deleteTreeStatus(String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    try (SqlSession session = sqlSessionFactory.openSession()) {
      session.getMapper(TreeStatusMapper.class).deleteTreeStatus();
      session.commit();
    }
  }
}
