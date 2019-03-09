package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import java.util.List;

public interface EventSearchingBean extends HeaderFieldSearchingBean {
  List<ChessGameMetadataDAO> searchByEventAndSite(String databaseName, String eventName, String site);
}
