package com.engwork.pgndb.chessgamesdownloader;

import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesloader.mappers.MovesMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class ChessGamesDownloaderInjector {

  @Bean
  public ChessGamesDownloaderBean chessGamesDownloaderBean() {
    return new ChessGamesDownloaderBeanImpl(gamesMapper, movesMapper, databaseContainer, gameMetadataExtracter);
  }

  private final ChessGamesMapper gamesMapper;
  private final MovesMapper movesMapper;
  private final GameMetadataExtracter gameMetadataExtracter;
  private final DatabaseContainer databaseContainer;

}
