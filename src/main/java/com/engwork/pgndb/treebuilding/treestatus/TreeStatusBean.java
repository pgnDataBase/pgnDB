package com.engwork.pgndb.treebuilding.treestatus;

public interface TreeStatusBean {

  TreeStatus getTreeStatus(String databaseName);

  void updateTreeStatus(String databaseName, TreeStatus treeStatus);

  void setTreeNotUpToDate(String databaseName);

  Boolean doesTreeExist(String databaseName);

  void deleteTreeStatus(String databaseName);
}
