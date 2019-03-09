package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import java.util.List;

public interface RoundSearchingBean extends HeaderFieldSearchingBean {
  List<ChessGameMetadataDAO> searchByRound(String databaseName, String round);
}
