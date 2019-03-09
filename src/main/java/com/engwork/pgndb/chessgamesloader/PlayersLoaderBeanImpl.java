package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.Player;
import com.engwork.pgndb.chessgamesloader.mappers.PlayersMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class PlayersLoaderBeanImpl implements PlayersLoaderBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  public Map<UUID, Player> insertWithIdCompetition(Map<UUID, Player> playersMap, String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    PlayersMapper playersMapper = sqlSession.getMapper(PlayersMapper.class);
    try {
      for (Player player : playersMap.values()) {
        UUID playerId = playersMapper.selectPlayerIdByName(player.getName());
        if (playerId == null) {
          player.setId(UUID.randomUUID());
          playersMapper.insertPlayer(player);
        } else {
          player.setId(playerId);
        }
      }
      sqlSession.commit();
      return playersMap;
    } finally {
      sqlSession.close();
    }
  }
}
