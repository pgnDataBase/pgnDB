package com.engwork.pgndb.chessdatabases;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChessDatabase {
  private UUID id;
  private String name;
  private Integer gamesTotal;
  private Boolean isShared;
  private String ownerUsername;
}
