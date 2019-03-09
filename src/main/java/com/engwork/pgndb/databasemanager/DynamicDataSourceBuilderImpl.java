package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.appconfig.databaseconfig.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;

@AllArgsConstructor
class DynamicDataSourceBuilderImpl implements DynamicDataSourceBuilder {
  private final DatabaseConfig databaseConfig;

  @Override
  public DataSource build(String databaseName) {
    String configUrl = databaseConfig.getUrl();
    String databaseUrl = configUrl.substring(0, configUrl.lastIndexOf("/") + 1);

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(databaseConfig.getDriver());
    hikariConfig.setUsername((databaseConfig.getUsername()));
    hikariConfig.setPassword(databaseConfig.getPassword());
    hikariConfig.setJdbcUrl(databaseUrl + databaseName);
    hikariConfig.setMaximumPoolSize(databaseConfig.getMaximumPoolSize());
    hikariConfig.setConnectionTimeout(databaseConfig.getConnectionTimeout());

    return new HikariDataSource(hikariConfig);
  }
}
