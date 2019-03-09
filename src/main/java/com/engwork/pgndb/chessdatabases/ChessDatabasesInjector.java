package com.engwork.pgndb.chessdatabases;

import com.engwork.pgndb.chessdatabases.mappers.ChessDatabasesMapper;
import com.engwork.pgndb.chessgames.ChessGamesBean;
import com.engwork.pgndb.databasesharing.mappers.DatabasesSharingMapper;
import com.engwork.pgndb.users.mappers.UsersMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
class ChessDatabasesInjector {

  private final ChessDatabasesMapper chessDatabasesMapper;
  private final DatabasesSharingMapper databasesSharingMapper;
  private final UsersMapper usersMapper;
  private final ChessGamesBean chessGamesBean;

  @Bean
  ChessDatabasesManagementBean getChessDatabasesManagementBean() {
    return new ChessDatabasesManagementBeanImpl(chessDatabasesMapper, chessGamesBean, usersMapper, databasesSharingMapper);
  }

  @Bean
  ChessDatabaseSearchingBean getChessDatabaseSearchingBean() {
    return new ChessDatabaseSearchingBeanImpl(chessDatabasesMapper);
  }
}
