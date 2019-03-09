package com.engwork.pgndb.moveediting;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.moveediting.moveparser.MoveParser;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class MoveValidatingBeanImpl implements MoveValidatingBean {
  private MoveParser moveParser;

  public Move parseMove(NewMoveRequest newMoveRequest){
    Move move;
    move = moveParser.parseMove(newMoveRequest);
    return move;
  }

  public void validateMovesGroup(List<Move> moveList){
    Move previousMove= moveList.get(0);
    for (Move move : moveList.subList(1,moveList.size())){
      moveParser.parseMove(new NewMoveRequest(previousMove,move));
      previousMove = move;
    }
  }
}
