package com.engwork.pgndb.chessengine;

import com.engwork.pgndb.appconfig.engineconfig.EngineConfig;
import com.engwork.pgndb.chessengine.engineinfo.EngineInfoManagingBean;
import com.engwork.pgndb.chessengine.engineinfo.EngineInfoManagingBeanImpl;
import com.engwork.pgndb.chessengine.wsclient.WebSocketClientBean;
import com.engwork.pgndb.chessengine.wsclient.WebSocketClientBeanImpl;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
class ChessEngineManagingInjector {

  private final EngineConfig engineConfig;
  private final SqlSessionFactory sqlSessionFactory;

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  WebSocketClientBean webSocketClientBean() {
    return new WebSocketClientBeanImpl(engineInfoManagingBean());
  }

  @Bean
  ChessEngineManagingBean chessEngineManagingBean() {
    return new ChessEngineManagingBeanImpl(restTemplate(), engineConfig, webSocketClientBean());
  }

  @Bean
  EngineInfoManagingBean engineInfoManagingBean() {
    return new EngineInfoManagingBeanImpl(sqlSessionFactory);
  }


}
