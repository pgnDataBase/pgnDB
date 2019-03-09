package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.pgnparser.entities.Comment;
import com.engwork.pgndb.pgnparser.entities.Entity;
import com.engwork.pgndb.pgnparser.entities.GameBegin;
import com.engwork.pgndb.pgnparser.entities.VariantBegin;
import com.engwork.pgndb.pgnparser.entities.VariantEnd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

class EntityListBuilder {
  List<Entity> entityList;

  private final MoveEntityBuilder moveEntityBuilder;

  public EntityListBuilder(MoveEntityBuilder moveEntityBuilder) {
    this.moveEntityBuilder = moveEntityBuilder;
  }

  public EntityListBuilder create() {
    entityList = new ArrayList<>();
    return this;
  }

  public List<Entity> build(List<Move> moveList) {
    HashSet<Integer> initializedVariants = new HashSet<>();
    entityList.add(new GameBegin());
    int moveIndex=0;
    for(Move move: moveList) {
      //Check if this move is firs move in variant
      if(move.getVariantId() != null && !initializedVariants.contains(move.getVariantId())) {
        entityList.add(new VariantBegin());
        initializedVariants.add(move.getVariantId());
      }
      if(!move.getMoveType().equals(MoveType.VEME.name()))
        entityList.add(moveEntityBuilder.create().build(move));
      //Check if this move contains comment
      if(move.getComment() != null ){
        entityList.add(new Comment(move.getComment()));
      }
      //Check if after this move variant is ending
      if(move.getVariantType()!=null && move.getVariantType().equals("VE")) {
        entityList.add(new VariantEnd());
      }
      moveIndex++;
    }
    return entityList;
  }

  private boolean isVariantFinished(List<Move> moveList, int from, Integer variantId) {
    Optional<Move> moveOpt = moveList.subList(from,moveList.size()).stream().filter(move ->move.getVariantId()!=null && move.getVariantId().equals(variantId)).findFirst();
    if(moveOpt.isPresent())
      return false;
      else return true;
  }
}
