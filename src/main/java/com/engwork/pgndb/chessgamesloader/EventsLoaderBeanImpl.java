package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.Event;
import com.engwork.pgndb.chessgamesloader.mappers.EventsMapper;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

class EventsLoaderBeanImpl implements EventsLoaderBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;

  EventsLoaderBeanImpl(SqlSessionFactory sqlSessionFactory, DatabaseContainer databaseContainer) {
    this.sqlSessionFactory = sqlSessionFactory;
    this.databaseContainer = databaseContainer;
  }

  @Override
  public Map<UUID, Event> insertWithIdCompetition(Map<UUID, Event> eventsMap, String databaseName) {
    databaseContainer.setDatabaseKey(databaseName);
    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
    EventsMapper eventsMapper = sqlSession.getMapper(EventsMapper.class);
    try {
      for (Event event : eventsMap.values()) {
        UUID eventId = eventsMapper.selectEventId(event);
        if (eventId == null) {
          event.setId(UUID.randomUUID());
          eventsMapper.insertEvent(event);
        } else {
          event.setId(eventId);
        }
      }
      sqlSession.commit();
      return eventsMap;
    } finally {
      sqlSession.close();
    }
  }
}
