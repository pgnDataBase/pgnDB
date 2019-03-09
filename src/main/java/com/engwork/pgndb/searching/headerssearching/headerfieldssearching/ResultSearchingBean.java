package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import java.util.List;

public interface ResultSearchingBean extends HeaderFieldSearchingBean {
  List<ChessGameMetadataDAO> searchByResult(String databaseName, String result);
}
