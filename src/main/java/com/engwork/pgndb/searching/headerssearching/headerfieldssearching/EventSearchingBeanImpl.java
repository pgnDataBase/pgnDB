package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesloader.mappers.EventsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.searching.requestmodel.HeaderFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class EventSearchingBeanImpl implements EventSearchingBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  @Override
  public List<ChessGameMetadataDAO> searchByEventAndSite(String databaseName, String eventName, String site) {
    databaseContainer.setDatabaseKey(databaseName);
    boolean isFilteringByEvent = eventName != null;
    boolean isFilteringBySite = site != null;

    List<ChessGameMetadataDAO> result = new ArrayList<>();
    if (isFilteringByEvent && isFilteringBySite) {
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        EventsMapper eventsMapper = sqlSession.getMapper(EventsMapper.class);
        List<UUID> eventIds = eventsMapper.selectEventIdsByNameAndSite(eventName, site);
        ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
        if (!eventIds.isEmpty()) {
          result = chessGamesMapper.selectChessGamesByManyEventIds(eventIds);
        }
      }
    } else if (isFilteringByEvent) {
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        EventsMapper eventsMapper = sqlSession.getMapper(EventsMapper.class);
        List<UUID> eventIds = eventsMapper.selectEventsIdsByName(eventName);
        ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
        if (!eventIds.isEmpty()) {
          result = chessGamesMapper.selectChessGamesByManyEventIds(eventIds);
        }
      }
    } else if (isFilteringBySite) {
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        EventsMapper eventsMapper = sqlSession.getMapper(EventsMapper.class);
        List<UUID> eventIds = eventsMapper.selectEventsIdsBySite(site);
        ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
        if (!eventIds.isEmpty()) {
          result = chessGamesMapper.selectChessGamesByManyEventIds(eventIds);
        }
      }
    }
    return result;
  }

  @Override
  public List<ChessGameMetadataDAO> execute(String databaseName, HeaderFilter headerFilter) {
    return searchByEventAndSite(databaseName, headerFilter.getEvent(), headerFilter.getSite());
  }

  @Override
  public Boolean appliesTo(HeaderFilter headerFilter) {
    return headerFilter.getEvent() != null || headerFilter.getSite() != null;
  }
}
