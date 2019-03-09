package com.engwork.pgndb.chessgames;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeleteGamesRequest {
  private List<UUID> gamesIds;
  private String databaseName;
}
