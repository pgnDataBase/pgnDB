package com.engwork.pgndb.searching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgamesdownloader.GameMetadataExtracter;
import com.engwork.pgndb.searching.headerssearching.HeadersSearchingBean;
import com.engwork.pgndb.searching.positionssearching.PositionsSearchingBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
class GamesSearchingInjector {

  private final GameMetadataExtracter gameMetadataExtracter;
  private final HeadersSearchingBean headersSearchingBean;
  private final PositionsSearchingBean positionsSearchingBean;
  private final IntersectionResolverBean<ChessGameMetadataDAO> intersectionResolverBean;

  @Bean
  GamesSearchingBean getGamesSearchingBean() {
    return new GamesSearchingBeanImpl(gameMetadataExtracter, headersSearchingBean, positionsSearchingBean, intersectionResolverBean);
  }

}
