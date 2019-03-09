package com.engwork.pgndb.chessgamesloader;

import com.engwork.pgndb.chessgamesconverter.model.Player;
import java.util.Map;
import java.util.UUID;

public interface PlayersLoaderBean {
  Map<UUID, Player> insertWithIdCompetition(Map<UUID, Player> players, String databaseName);
}
