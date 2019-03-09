package com.engwork.pgndb.chessgamesconverter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChessGameData {
  private ChessGameMetadata chessGameMetadata;
  private List<Move> moveList = new ArrayList<>();
  private List<Position> positionList = new ArrayList<>();
  private String gameDescription;
  private Boolean notPersisted;
}
