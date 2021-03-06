package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import java.util.List;

public interface BlackPlayerSearchingBean extends HeaderFieldSearchingBean {
  List<ChessGameMetadataDAO> searchByBlackPlayer(String databaseName, String blackPlayerName);
}
