package com.engwork.pgndb.chessgames;

import com.engwork.pgndb.chessgamesdownloader.GameMetadataExtracter;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Data
class ChessGamesInjector {

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
  private final GameMetadataExtracter gameMetadataExtracter;

  @Autowired
  @Lazy
  public ChessGamesInjector(DatabaseContainer databaseContainer,
                            @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory,
                            GameMetadataExtracter gameMetadataExtracter) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
    this.gameMetadataExtracter = gameMetadataExtracter;
  }

  @Bean
  @Lazy
  ChessGamesBean chessGamesBean() {
    return new ChessGamesBeanImpl(databaseContainer, sqlSessionFactory, gameMetadataExtracter);
  }

}
