package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import java.util.List;

public interface WhitePlayerSearchingBean extends HeaderFieldSearchingBean {
  List<ChessGameMetadataDAO> searchByWhitePlayer(String databaseName, String whitePlayerName);
}
