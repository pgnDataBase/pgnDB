package com.engwork.pgndb.databasemanager;

import com.engwork.pgndb.chessdatabases.ChessDatabaseSearchingBean;
import com.engwork.pgndb.chessdatabases.databasevalidation.DatabaseValidationBean;
import com.engwork.pgndb.databasesharing.DatabaseSharingBean;
import com.engwork.pgndb.users.UsersBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
class DatabaseManagerInjector {

  private final String DATA_SOURCE_NAME = "dynamic.data.source";

  @Autowired
  @Qualifier(value = DATA_SOURCE_NAME)
  private DynamicDataSource dataSource;
  private final DatabaseContainer databaseContainer;
  private final DynamicDataSourceBuilder dynamicDataSourceBuilder;
  private final UsersBean usersBean;
  private final DatabaseSharingBean databaseSharingBean;
  private final DatabaseValidationBean databaseValidationBean;

  @Lazy
  private final ChessDatabaseSearchingBean chessDatabaseSearchingBean;

  @Lazy
  private final PSQLManager psqlManager;

  @Autowired
  public DatabaseManagerInjector(DatabaseContainer databaseContainer,
                                 DynamicDataSourceBuilder dynamicDataSourceBuilder,
                                 UsersBean usersBean,
                                 DatabaseSharingBean databaseSharingBean,
                                 DatabaseValidationBean databaseValidationBean,
                                 ChessDatabaseSearchingBean chessDatabaseSearchingBean,
                                 PSQLManager psqlManager
  ) {
    this.databaseContainer = databaseContainer;
    this.dynamicDataSourceBuilder = dynamicDataSourceBuilder;
    this.usersBean = usersBean;
    this.databaseSharingBean = databaseSharingBean;
    this.databaseValidationBean = databaseValidationBean;
    this.chessDatabaseSearchingBean = chessDatabaseSearchingBean;
    this.psqlManager = psqlManager;
  }

  @Bean
  @Lazy
  DatabaseManager getDatabaseManager() {
    return new DatabaseManagerImpl(dataSource,
                                   chessDatabaseSearchingBean,
                                   databaseSharingBean,
                                   psqlManager,
                                   databaseContainer,
                                   dynamicDataSourceBuilder,
                                   usersBean,
                                   databaseValidationBean
    );
  }
}
