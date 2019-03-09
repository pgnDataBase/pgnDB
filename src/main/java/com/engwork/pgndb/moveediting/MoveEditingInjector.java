package com.engwork.pgndb.moveediting;

import com.engwork.pgndb.chessgamesconverter.PositionBuilder;
import com.engwork.pgndb.chessgamesloader.ChessGamesLoaderBean;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.moveediting.moveparser.MoveParser;
import com.engwork.pgndb.pgnparser.PossibleMovesProvider;
import com.engwork.pgndb.pgnparser.PossibleMovesProviderImpl;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatusBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MoveEditingInjector {

  @Autowired
  MoveEditingInjector(DatabaseContainer databaseContainer,
                      @Qualifier("dynamic.session.factory") SqlSessionFactory sqlSessionFactory, PositionBuilder positionBuilder, ChessGamesLoaderBean chessGamesLoaderBean, TreeStatusBean treeStatusBean) {
    this.databaseContainer = databaseContainer;
    this.sqlSessionFactory = sqlSessionFactory;
    this.positionBuilder = positionBuilder;
    this.chessGamesLoaderBean = chessGamesLoaderBean;
    this.treeStatusBean = treeStatusBean;
  }

  @Bean
  MoveParser moveParser() {
    return new MoveParser(possibleMovesProvider());
  }

  @Bean
  MoveValidatingBean moveValidatingBean() {
    return new MoveValidatingBeanImpl(moveParser());
  }

  @Bean
  PossibleMovesProvider possibleMovesProvider() {
    return new PossibleMovesProviderImpl();
  }

  @Bean
  public MoveSavingBean moveSavingBean() {
    return new MoveSavingBeanImpl(sqlSessionFactory, databaseContainer, moveValidatingBean(), positionBuilder, chessGamesLoaderBean, treeStatusBean);
  }

  private final DatabaseContainer databaseContainer;
  private final SqlSessionFactory sqlSessionFactory;
  private final PositionBuilder positionBuilder;
  private final ChessGamesLoaderBean chessGamesLoaderBean;
  private final TreeStatusBean treeStatusBean;

}
