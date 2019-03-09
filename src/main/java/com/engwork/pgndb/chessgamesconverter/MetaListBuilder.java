package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.pgnparser.pgn.Meta;

import java.util.ArrayList;
import java.util.List;

public class MetaListBuilder {
  private final static String EVENT_NAME="Event";
  private final static String EVENT_DATE="Date";
  private final static String SITE_NAME="Site";
  private final static String WHITE_PLAYER="White";
  private final static String BLACK_PLAYER="Black";
  private final static String RESULT="Result";
  private final static String ROUND ="Round";
  List<Meta> metaList;

  public MetaListBuilder create(){
    metaList = new ArrayList<>();
    return this;
  }

  public List<Meta> build(ChessGameMetadata chessGameMetadata) {
    processGameMetaData(chessGameMetadata);
    return this.metaList;
  }

  private void processGameMetaData(ChessGameMetadata chessGameMetadata){
    metaList.add(new Meta(EVENT_NAME,chessGameMetadata.getEvent(),true));
    metaList.add(new Meta(SITE_NAME,chessGameMetadata.getSite(),true));
    metaList.add(new Meta(EVENT_DATE,chessGameMetadata.getDate(),true));

    metaList.add(new Meta(WHITE_PLAYER,chessGameMetadata.getWhitePlayer(),true));

    metaList.add(new Meta(BLACK_PLAYER,chessGameMetadata.getBlackPlayer(),true));

    metaList.add(new Meta(RESULT,chessGameMetadata.getResult(),true));

    metaList.add(new Meta(ROUND,chessGameMetadata.getRound(),true));

    for (String key:chessGameMetadata.getAdditional().keySet()){
      metaList.add(new Meta(key,chessGameMetadata.getAdditional().get(key),false));
    }
  }

}
