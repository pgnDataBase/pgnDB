package com.engwork.pgndb.chessgamesconverter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Position {
  private UUID id;
  private String fen;
  private Integer whitePawnsCount;
  private Integer blackPawnsCount;
  private Integer whitePiecesCount;
  private Integer blackPiecesCount ;
}
