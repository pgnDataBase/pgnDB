package com.engwork.pgndb.chessgamesconverter;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class ChessGamesConverterInjector {

  //From PGN parser representation to our model
  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public MoveBuilder moveBuilder() {
    return new MoveBuilder();
  }


  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public PositionBuilder positionBuilder() {
    return new PositionBuilder();
  }

  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public ChessGameDataBuilder chessGameDataBuilder() {
    return new ChessGameDataBuilder(positionBuilder(), moveBuilder());
  }

  //From our model to PGN parser representation
  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public PGNGameBuilder pgnGameBuilder() {
    return new PGNGameBuilder(metaListBuilder(),entityListBuilder());
  }

  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public MetaListBuilder metaListBuilder() {
    return new MetaListBuilder();
  }

  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public EntityListBuilder entityListBuilder() {
    return new EntityListBuilder(moveEntityBuilder());
  }

  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public MoveEntityBuilder moveEntityBuilder() {
    return new MoveEntityBuilder();
  }

  //Chess game reader
  @Bean
  public ChessGamesConverter chessGamesReader() {
    return new ChessGamesConverter(chessGameDataBuilder(),pgnGameBuilder());
  }
}
