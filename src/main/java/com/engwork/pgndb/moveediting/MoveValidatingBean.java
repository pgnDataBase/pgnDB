package com.engwork.pgndb.moveediting;

import com.engwork.pgndb.chessgamesconverter.model.Move;

import java.util.List;

interface MoveValidatingBean {

 Move parseMove(NewMoveRequest newMoveRequest);
 void validateMovesGroup(List<Move> moveList);
}
