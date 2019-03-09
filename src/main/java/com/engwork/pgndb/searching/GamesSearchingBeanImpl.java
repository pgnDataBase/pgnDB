package com.engwork.pgndb.searching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMetadataResponse;
import com.engwork.pgndb.chessgames.ChessGamesRequest.ChessGamesMetadataRequest;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesdownloader.GameMetadataExtracter;
import com.engwork.pgndb.searching.headerssearching.HeadersSearchingBean;
import com.engwork.pgndb.searching.positionssearching.PositionsSearchingBean;
import com.engwork.pgndb.searching.requestmodel.GamesSearchingFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class GamesSearchingBeanImpl implements GamesSearchingBean {
  private final GameMetadataExtracter gameMetadataExtracter;
  private final HeadersSearchingBean headersSearchingBean;
  private final PositionsSearchingBean positionsSearchingBean;
  private final IntersectionResolverBean<ChessGameMetadataDAO> intersectionResolverBean;

  @Override
  public ChessGamesMetadataResponse search(ChessGamesMetadataRequest chessGamesMetadataRequest) {
    String databaseName = chessGamesMetadataRequest.getDatabaseName();
    GamesSearchingFilter gamesSearchingFilter = chessGamesMetadataRequest.getFilter();

    List<ChessGameMetadataDAO> result = searchWithFilterApplied(databaseName, gamesSearchingFilter);
    result = result.stream().sorted(Comparator.comparing(ChessGameMetadataDAO::getId)).collect(Collectors.toList());

    Integer limit = chessGamesMetadataRequest.getPageSize();
    Integer offset = chessGamesMetadataRequest.getOffset();
    if (offset > result.size() - 1) {
      return new ChessGamesMetadataResponse(Collections.emptyList(), result.size());
    }

    List<ChessGameMetadataDAO> resultWithLimitAndOffset = new ArrayList<>();
    for (int index = offset; index < offset + limit; index++) {
      if (index > result.size() - 1) {
        break;
      }
      resultWithLimitAndOffset.add(result.get(index));
    }
    return new ChessGamesMetadataResponse(mapToResult(databaseName, resultWithLimitAndOffset), result.size());
  }

  private List<ChessGameMetadataDAO> searchWithFilterApplied(String databaseName, GamesSearchingFilter gamesSearchingFilter) {
    Boolean isFilteringByHeaders = gamesSearchingFilter.getHeaderFilter() != null;
    Boolean isFilteringByPositions = gamesSearchingFilter.getPositionsFilter() != null;
    if (isFilteringByHeaders && isFilteringByPositions) {
      List<ChessGameMetadataDAO> headersResult = headersSearchingBean.search(databaseName, gamesSearchingFilter.getHeaderFilter());
      if (headersResult.isEmpty()) {
        return Collections.emptyList();
      }
      List<ChessGameMetadataDAO> positionsResult = positionsSearchingBean.search(databaseName, gamesSearchingFilter.getPositionsFilter());
      return intersectionResolverBean.resolve(headersResult, positionsResult);

    } else if (isFilteringByHeaders) {
      return headersSearchingBean.search(databaseName, gamesSearchingFilter.getHeaderFilter());
    } else if (isFilteringByPositions) {
      return positionsSearchingBean.search(databaseName, gamesSearchingFilter.getPositionsFilter());
    } else {
      return Collections.emptyList();
    }
  }

  private List<ChessGameMetadata> mapToResult(String databaseName, List<ChessGameMetadataDAO> list) {
    List<ChessGameMetadata> result = new ArrayList<>();
    for (ChessGameMetadataDAO chessGameMetadataDAO : list) {
      result.add(gameMetadataExtracter.extract(databaseName, chessGameMetadataDAO));
    }
    return result;
  }
}
