package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import java.util.List;

public interface DateSearchingBean extends HeaderFieldSearchingBean {
  List<ChessGameMetadataDAO> searchByDate(String databaseName, String date);
}
