package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.Position;
import com.engwork.pgndb.chessgamesconverter.model.VariantMoveType;
import com.engwork.pgndb.pgnparser.entities.Comment;
import com.engwork.pgndb.pgnparser.entities.Entity;
import com.engwork.pgndb.pgnparser.entities.VariantBegin;
import com.engwork.pgndb.pgnparser.entities.VariantEnd;
import com.engwork.pgndb.pgnparser.pgn.PGNGame;

import java.util.List;
import java.util.Stack;

class ChessGameDataBuilder {
  private ChessGameData chessGameData;
  private final PositionBuilder positionBuilder;
  private final MoveBuilder moveBuilder;

  public ChessGameDataBuilder( PositionBuilder positionBuilder, MoveBuilder moveBuilder) {
    this.positionBuilder = positionBuilder;
    this.moveBuilder = moveBuilder;
  }

  public ChessGameDataBuilder create() {
    this.chessGameData = new ChessGameData();
    return this;
  }

  public ChessGameData build(PGNGame pgnGame) {
    processEntities(pgnGame);
    ChessGameMetadata chessGameMetadata = ChessGameMetadata.create(pgnGame.getMeta());
    chessGameData.setChessGameMetadata(chessGameMetadata);
    return chessGameData;
  }
  private Move getLastFromVariant(Integer variantId){
    int moveListSize = chessGameData.getMoveList().size();
    for(int index=moveListSize-1; index>=0;index--){
      Move move = chessGameData.getMoveList().get(index);
      if(move.getVariantId().equals(variantId))
        return move;
    }
    return null;
  }

  private void processEntities(PGNGame pgnGame) {
    Stack<Integer> variantStack = new Stack<>();
    Integer variantsCount=0;
    boolean variantInitializer=false;
    List<Entity> entities = pgnGame.getEntities();
    Integer entityNumber=1;
    for (Entity entity : entities) {
      if (entity instanceof com.engwork.pgndb.pgnparser.entities.Move) {
        processMove(entity, variantStack,variantInitializer);
        getLastAddedMove().setEntityNumber(entityNumber);
        entityNumber++;
        variantInitializer=false;
      }
      if (entity instanceof VariantBegin) {
        variantsCount++;
        variantStack.push(variantsCount);
        variantInitializer=true;
      }
      if (entity instanceof VariantEnd) {
        Integer lastVariantId  = variantStack.pop();
        chessGameData.getMoveList().add(moveBuilder.create().buildVariantEnd(lastVariantId,entityNumber));
        entityNumber++;
      }
      if (entity instanceof Comment) {
        processComment(entity);
      }
    }
  }

  private Move getLastAddedMove() {
    int moveListSize = chessGameData.getMoveList().size();
    return chessGameData.getMoveList().get(moveListSize - 1);
  }


  private void processComment(Entity entity) {
    Comment comment = (Comment) entity;
    try {
      getLastAddedMove().setComment(comment.getValue());
    } catch (Exception e) {
      chessGameData.setGameDescription(comment.getValue());
    }
  }

  private void processMove(Entity entity, Stack<Integer> variantStack,boolean variantInitializer) {
    Move move = moveBuilder.create().build((com.engwork.pgndb.pgnparser.entities.Move) entity);
    chessGameData.getMoveList().add(move);
    Position position = positionBuilder.create().build(((com.engwork.pgndb.pgnparser.entities.Move) entity));
    if (!variantStack.isEmpty()) {
      if(variantInitializer) {
        getLastAddedMove().setVariantType(VariantMoveType.VARIANT_BEGIN.value);
      } else {
        getLastAddedMove().setVariantType(VariantMoveType.VARIANT_INSIDE.value);
      }
      getLastAddedMove().setVariantId(variantStack.peek());
    }
    chessGameData.getPositionList().add(position);
    getLastAddedMove().setFen(position.getFen());
  }
}
