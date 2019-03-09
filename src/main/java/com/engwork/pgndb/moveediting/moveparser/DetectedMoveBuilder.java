package com.engwork.pgndb.moveediting.moveparser;

import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;
import com.engwork.pgndb.chessgamesconverter.model.Field;
import com.engwork.pgndb.moveediting.NewMoveRequest;
import com.engwork.pgndb.chessgamesconverter.FieldUtils;

class DetectedMoveBuilder {
  private DetectedMove detectedMove;
  public DetectedMoveBuilder create(){
    detectedMove = null;
    return this;
  }

  public DetectedMove build(NewMoveRequest newMoveRequest){
    Field from = FieldUtils.parse(newMoveRequest.getNewMove().getFromField());
    Field to = FieldUtils.parse(newMoveRequest.getNewMove().getToField());
    String pieceCode = newMoveRequest.getNewMove().getPieceCode();
    Integer movedPiece = PieceColor.getByColorCode(pieceCode.substring(0,1)).value + Piece.getByPieceCode(pieceCode.substring(1)).getValue();
    detectedMove =  new DetectedMove(from.getXPos(),from.getYPos(),to.getXPos(),to.getYPos(),movedPiece);
    return detectedMove;
  }
}
