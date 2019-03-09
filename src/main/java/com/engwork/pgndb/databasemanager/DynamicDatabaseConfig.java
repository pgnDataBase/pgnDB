package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.chessdatabases.ChessDatabase;
import com.engwork.pgndb.chessdatabases.ChessDatabaseSearchingBean;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@MapperScan(
    basePackages = {
        "com.engwork.pgndb.chessgames",
        "com.engwork.pgndb.chessgamesloader.mappers",
        "com.engwork.pgndb.treebuilding.mapper"
    },
    sqlSessionFactoryRef = DynamicDatabaseConfig.SQL_SESSION_FACTORY_NAME
)
class DynamicDatabaseConfig {

  private static final String DATA_SOURCE_NAME = "dynamic.data.source";
  static final String SQL_SESSION_FACTORY_NAME = "dynamic.session.factory";

  private final ChessDatabaseSearchingBean chessDatabaseSearchingBean;
  private final DatabaseContainer databaseContainer;
  private final DynamicDataSourceBuilder dynamicDataSourceBuilder;
  private final SessionConfigProvider sessionConfigProvider;

  @Bean(name = DATA_SOURCE_NAME)
  DynamicDataSource dataSource() {
    DynamicDataSource dynamicDataSource = new DynamicDataSource(databaseContainer);

    Map<Object, Object> dataSourceMap = new HashMap<>();
    for (ChessDatabase chessDatabase : chessDatabaseSearchingBean.getAll()) {
      DataSource dataSource = dynamicDataSourceBuilder.build(chessDatabase.getName());
      dataSourceMap.put(chessDatabase.getName(), dataSource);
      databaseContainer.addDatabaseKey(chessDatabase.getName());
    }
    dynamicDataSource.setTargetDataSources(dataSourceMap);
    return dynamicDataSource;
  }

  @Bean(name = SQL_SESSION_FACTORY_NAME)
  SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource());
    sqlSessionFactoryBean.setConfiguration(sessionConfigProvider.getConfig());
    return sqlSessionFactoryBean.getObject();
  }
}
