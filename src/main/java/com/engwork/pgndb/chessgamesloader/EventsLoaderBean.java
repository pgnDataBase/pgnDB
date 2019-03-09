package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.Event;

import java.util.Map;
import java.util.UUID;

public interface EventsLoaderBean {
  Map<UUID, Event> insertWithIdCompetition(Map<UUID, Event> eventsMap, String databaseName);
}
