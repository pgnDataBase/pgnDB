package com.engwork.pgndb.searching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IntersectionResolverInjector {

  @Bean
  IntersectionResolverBean<ChessGameMetadataDAO> getIntersectionResolverBean() {
    return new IntersectionResolverBeanImpl<>();
  }
}
