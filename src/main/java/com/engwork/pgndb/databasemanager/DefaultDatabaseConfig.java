package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.appconfig.databaseconfig.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
@MapperScan(
    basePackages = {
        "com.engwork.pgndb.chessdatabases.mappers",
        "com.engwork.pgndb.users.mappers",
        "com.engwork.pgndb.userssettings.mappers",
        "com.engwork.pgndb.databasesharing.mappers",
        "com.engwork.pgndb.chessengine.engineinfo"
    },
    sqlSessionFactoryRef = DefaultDatabaseConfig.SQL_SESSION_FACTORY_NAME
)
class DefaultDatabaseConfig {

  private static final String DATA_SOURCE_NAME = "default.data.source";
  static final String SQL_SESSION_FACTORY_NAME = "default.session.factory";

  private final DatabaseConfig databaseConfig;
  private final SessionConfigProvider sessionConfigProvider;

  @Bean(name = DATA_SOURCE_NAME)
  public DataSource defaultDataSource() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(databaseConfig.getDriver());
    hikariConfig.setUsername((databaseConfig.getUsername()));
    hikariConfig.setPassword(databaseConfig.getPassword());
    hikariConfig.setJdbcUrl(databaseConfig.getUrl());
    hikariConfig.setMaximumPoolSize(databaseConfig.getMaximumPoolSize());
    hikariConfig.setConnectionTimeout(databaseConfig.getConnectionTimeout());

    return new HikariDataSource(hikariConfig);
  }


  @Primary
  @Bean(name = SQL_SESSION_FACTORY_NAME)
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(defaultDataSource());
    sqlSessionFactoryBean.setConfiguration(sessionConfigProvider.getConfig());
    return sqlSessionFactoryBean.getObject();
  }
}
