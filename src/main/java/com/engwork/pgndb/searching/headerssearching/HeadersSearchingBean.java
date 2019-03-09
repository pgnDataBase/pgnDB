package com.engwork.pgndb.searching.headerssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.searching.requestmodel.HeaderFilter;
import java.util.List;

public interface HeadersSearchingBean {
  List<ChessGameMetadataDAO> search(String databaseName, HeaderFilter headerFilter);
}
