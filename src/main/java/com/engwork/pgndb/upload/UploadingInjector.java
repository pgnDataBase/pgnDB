package com.engwork.pgndb.upload;

import com.engwork.pgndb.chessgamesconverter.ChessGamesConverter;
import com.engwork.pgndb.chessgamesloader.ChessGamesLoaderBean;
import com.engwork.pgndb.statusresolver.StatusResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
class UploadingInjector {

  private final ChessGamesConverter chessGamesConverter;
  private ChessGamesLoaderBean chessGamesLoaderBean;
  private StatusResolver loadingStatusResolver;

  @Lazy
  @Autowired
  public UploadingInjector(ChessGamesLoaderBean chessGamesLoaderBean, ChessGamesConverter chessGamesConverter,
                           @Qualifier("loading.status.resolver") StatusResolver loadingStatusResolver) {
    this.chessGamesLoaderBean = chessGamesLoaderBean;
    this.chessGamesConverter = chessGamesConverter;
    this.loadingStatusResolver = loadingStatusResolver;
  }

  @Bean
  FileToChessGamesConverterBean fileToChessGamesConverterBean() {
    return new FileToChessGamesConverterBeanImpl(chessGamesConverter);
  }

  @Bean
  ChessGamesToDbLoaderBean chessGamesToDbLoaderBean() {
    return new ChessGamesToDbLoaderBeanImpl(fileToChessGamesConverterBean(), chessGamesLoaderBean, loadingStatusResolver);
  }
}
