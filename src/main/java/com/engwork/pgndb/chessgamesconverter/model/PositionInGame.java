package com.engwork.pgndb.chessgamesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionInGame {
  private UUID id;
  private UUID gameId;
  private Integer positionNumber;
  private UUID positionId;
}
