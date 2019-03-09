package com.engwork.pgndb.chessgameseditor;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChessGamesEditorInjector {

  @Bean
  public ChessGamesEditorBean chessGamesEditorBean(){
    return new ChessGamesEditorBeanImpl(databaseContainer,sqlSessionFactory);
  }


  @Autowired
  public ChessGamesEditorInjector(DatabaseContainer databaseContainer, @Qualifier("dynamic.session.factory")SqlSessionFactory sqlSessionFactory){
    this.databaseContainer =databaseContainer;
    this.sqlSessionFactory =sqlSessionFactory;
  }

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
}
