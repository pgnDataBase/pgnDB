package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.pgnparser.entities.Entity;
import com.engwork.pgndb.pgnparser.pgn.Meta;
import com.engwork.pgndb.pgnparser.pgn.PGNGame;

import java.util.List;

class PGNGameBuilder {
  private PGNGame pgnGame;
  private final MetaListBuilder metaListBuilder;
  private final EntityListBuilder entityListBuilder;

  public PGNGameBuilder(MetaListBuilder metaListBuilder, EntityListBuilder entityListBuilder){
    this.metaListBuilder = metaListBuilder;
    this.entityListBuilder =  entityListBuilder;
  }

  public PGNGameBuilder create(){
    this.pgnGame = new PGNGame();
    return this;
  }

  public PGNGame build(ChessGameData chessGameData) {
    ChessGameMetadata chessGameMetadata =chessGameData.getChessGameMetadata();

    List<Meta> metaList = metaListBuilder.create().build(chessGameMetadata);
    pgnGame.setMeta(metaList);

    List<Entity> entityList = entityListBuilder.create().build(chessGameData.getMoveList());
    pgnGame.setEntities(entityList);

    return pgnGame;
  }
}
