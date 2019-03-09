package com.engwork.pgndb.databasemanager;

import javax.sql.DataSource;

interface DynamicDataSourceBuilder {
  DataSource build(String databaseName);
}
