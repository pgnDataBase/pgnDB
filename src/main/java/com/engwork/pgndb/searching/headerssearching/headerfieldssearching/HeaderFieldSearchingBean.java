package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.searching.requestmodel.HeaderFilter;
import java.util.List;

public interface HeaderFieldSearchingBean {
  List<ChessGameMetadataDAO> execute(String databaseName, HeaderFilter headerFilter);

  Boolean appliesTo(HeaderFilter headerFilter);
}
