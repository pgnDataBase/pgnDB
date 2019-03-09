package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import java.util.List;
import java.util.Map;

public interface AdditionalSearchingBean extends HeaderFieldSearchingBean {
  List<ChessGameMetadataDAO> searchByAdditionalPlayer(String databaseName, Map<String, String> additionalTags);
}
