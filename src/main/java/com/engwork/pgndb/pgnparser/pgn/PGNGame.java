package com.engwork.pgndb.pgnparser.pgn;


import com.engwork.pgndb.pgnparser.entities.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
//import chess.mobile.util.MetadataOrderer;

/**
 * Created by Stanisław Kabaciński.
 */

@Data
@NoArgsConstructor
public class PGNGame {

  private int id;

  private List<Meta> meta= new ArrayList<>();

  private List<Entity> entities= new ArrayList<>();

  public PGNGame(List<Meta> meta, List<Entity> entities) {
    this.meta = meta;
    this.entities = entities;
  }
}
