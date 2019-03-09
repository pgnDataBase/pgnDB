package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.statusresolver.StatusResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
class ChessGamesLoaderInjector {
  private final EventsLoaderBean eventsLoaderBean;
  private final PlayersLoaderBean playersLoaderBean;
  private final MetadataLoaderBean metadataLoaderBean;
  private final MovesLoaderBean movesLoaderBean;
  private final PositionsLoaderBean positionsLoaderBean;
  private final TagsLoaderBean tagsLoaderBean;
  private final StatusResolver loadingStatusResolver;

  public ChessGamesLoaderInjector(EventsLoaderBean eventsLoaderBean, PlayersLoaderBean playersLoaderBean,
                                  MetadataLoaderBean metadataLoaderBean, MovesLoaderBean movesLoaderBean,
                                  PositionsLoaderBean positionsLoaderBean, TagsLoaderBean tagsLoaderBean,
                                  @Qualifier("loading.status.resolver") StatusResolver loadingStatusResolver) {
    this.eventsLoaderBean = eventsLoaderBean;
    this.playersLoaderBean = playersLoaderBean;
    this.metadataLoaderBean = metadataLoaderBean;
    this.movesLoaderBean = movesLoaderBean;
    this.positionsLoaderBean = positionsLoaderBean;
    this.tagsLoaderBean = tagsLoaderBean;
    this.loadingStatusResolver = loadingStatusResolver;
  }

  @Bean
  @Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public ChessGamesLoaderBean getChessGamesLoaderBean() {
    return new ChessGamesLoaderBeanImpl(this.eventsLoaderBean, this.playersLoaderBean, this.metadataLoaderBean, this.positionsLoaderBean, this.movesLoaderBean, this.tagsLoaderBean, this.loadingStatusResolver);
  }
}

