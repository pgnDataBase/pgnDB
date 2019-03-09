package com.engwork.pgndb.databasemerging;

import com.engwork.pgndb.chessgamesconverter.ChessGamesConverter;
import com.engwork.pgndb.chessgamesdownloader.ChessGamesDownloaderBean;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.upload.ChessGamesToDbLoaderBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DatabaseMergingInjector {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;

  private final ChessGamesDownloaderBean chessGamesDownloaderBean;
  private final ChessGamesConverter chessGamesConverter;
  private final ChessGamesToDbLoaderBean chessGamesToDbLoaderBean;

  @Autowired
  DatabaseMergingInjector(DatabaseContainer databaseContainer,
                          @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory,
                          ChessGamesDownloaderBean chessGamesDownloaderBean,
                          ChessGamesConverter chessGamesConverter,
                          ChessGamesToDbLoaderBean chessGamesToDbLoaderBean) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
    this.chessGamesDownloaderBean = chessGamesDownloaderBean;
    this.chessGamesConverter = chessGamesConverter;
    this.chessGamesToDbLoaderBean = chessGamesToDbLoaderBean;
  }

  @Bean
  DatabaseMergingBean getDatabaseMergingBean() {
    return new DatabaseMergingBeanImpl(sqlSessionFactory,
                                       databaseContainer,
                                       chessGamesDownloaderBean,
                                       chessGamesConverter,
                                       chessGamesToDbLoaderBean
    );
  }
}
