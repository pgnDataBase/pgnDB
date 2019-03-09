package com.engwork.pgndb.databasemerging;

import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesconverter.ChessGamesConverter;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesdownloader.ChessGamesDownloaderBean;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.upload.ChessGamesToDbLoaderBean;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class DatabaseMergingBeanImpl implements DatabaseMergingBean {

  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  private final ChessGamesDownloaderBean chessGamesDownloaderBean;
  private final ChessGamesConverter chessGamesConverter;
  private final ChessGamesToDbLoaderBean chessGamesToDbLoaderBean;


  @Override
  public void merge(String from, String to) {
    databaseContainer.setDatabaseKey(from);
    StringBuilder stringBuilder = new StringBuilder();
    List<UUID> gamesIds;
    try (SqlSession session = sqlSessionFactory.openSession()) {
      ChessGamesMapper chessGamesMapper = session.getMapper(ChessGamesMapper.class);
      gamesIds = chessGamesMapper.selectChessGamesIds();
    }
    gamesIds.forEach(id -> {
      ChessGameData chessGameData = chessGamesDownloaderBean.getChessGameDataById(id, from);
      stringBuilder.append(chessGamesConverter.convertChessGameDataIntoPGN(chessGameData));
      stringBuilder.append("\n");
    });
    if (stringBuilder.length() > 0) {
      chessGamesToDbLoaderBean.loadGamesToDatabase(stringBuilder.toString(), to);
    }
  }
}
