package com.engwork.pgndb.chessgames;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChessGamesMetadataResponse {
  private List<ChessGameMetadata> games;
  private Integer total;
}
