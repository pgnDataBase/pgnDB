package com.engwork.pgndb.chessengine.engineinfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EngineInfo {
  private String fen;
  private Integer depth;
  private Double scoreCentiPawn;
  private String time;
  private String movesChain;
}
