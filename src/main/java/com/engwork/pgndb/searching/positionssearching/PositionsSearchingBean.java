package com.engwork.pgndb.searching.positionssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.searching.requestmodel.PositionsFilter;
import java.util.List;

public interface PositionsSearchingBean {
  List<ChessGameMetadataDAO> search(String databaseName, PositionsFilter positionsFilter);
}
