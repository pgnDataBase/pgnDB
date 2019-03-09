package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import com.engwork.pgndb.pgnparser.consts.Piece;
import com.engwork.pgndb.pgnparser.entities.Move;
import lombok.Data;

class MoveEntityBuilder {
  private Move move;

  public MoveEntityBuilder create() {
    move = new Move();
    return this;
  }

  public Move build(com.engwork.pgndb.chessgamesconverter.model.Move moveModel) {
    move.setSan(moveModel.getSan());
    move.setNo(moveModel.getMoveNumber());
    parseFields(moveModel.getFromField(),moveModel.getToField());
    parseMoveType(moveModel.getMoveType());
    parsePieces(moveModel.getPieceCode(),moveModel.getCapturedPieceCode(),moveModel.getPromotedPieceCode());
    return move;
  }

  private void parsePieces(String movedPieceCode, String capturedPieceCode, String promotedPieceCode) {
    ParserPiece movedParserPiece = new ParserPiece(movedPieceCode);
    move.setColor(movedParserPiece.color);
    move.setPiece(movedParserPiece.code);

    if(capturedPieceCode != null) {
      ParserPiece capturedParserPiece = new ParserPiece(capturedPieceCode);
      move.setCapturedPiece(capturedParserPiece.color + capturedParserPiece.code);
    }

    if(promotedPieceCode != null ) {
      ParserPiece promotedParserPiece = new ParserPiece(promotedPieceCode);
      move.setPromotion(promotedParserPiece.code);
    }
  }
  private void parseMoveType(String moveType) {
    if(moveType.equals(MoveType.ENPT.name())){
      move.setEnPassant(true);
    }
    if(moveType.equals(MoveType.KSCG.name())){
      move.setKSCastling(true);
    }
    if(moveType.equals(MoveType.QSCG.name())){
      move.setQSCastling(true);
    }
  }

  private void parseFields(String startField, String finalField) {
    Field sField = new Field(startField);
    move.setFromX(sField.xPos);
    move.setFromY(sField.yPos);

    Field fField = new Field(finalField);
    move.setToX(fField.xPos);
    move.setToY(fField.yPos);
  }

  @Data
  private class Field{
    int xPos;
    int yPos;
    Character[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    Field(String fieldCode){
      this.xPos = getNumericRepresentation(fieldCode.charAt(0));
      this.yPos = Integer.parseInt(Character.toString(fieldCode.charAt(1)));
    }
    int getNumericRepresentation(Character character) {
      for (int index = 0; index < letters.length;index++){
        if(letters[index].equals(character))
          return index;
      }
      return -1;
    }
  }

  @Data
  private class ParserPiece {
    Integer color;
    Integer code;
    ParserPiece(String pieceAndColorCode){
      //Extract color
      assert(pieceAndColorCode != null);
      String colorCode = Character.toString(pieceAndColorCode.charAt(0));
      this.color = PieceColor.getByColorCode(colorCode).value;
      //Extract piece Code
      String pieceCode = pieceAndColorCode.substring(1);
      if (!pieceCode.equals("RK")){
        this.code = com.engwork.pgndb.chessgamesconverter.model.Piece.getByPieceCode(pieceCode).getValue();
      } else {
        /*For us it doesn't matter what type of rook it is, but for parser 
        * it might be important, especially when castling move is validated.
        * Castling move is possible only when rook wasn't moved before 
        * so it's enough for us to have valid rook types only in initial positions 
         */
        if(move.getFromX() < 4 ){
          this.code = Piece.QS_ROOK;
        } else {
          this.code = Piece.KS_ROOK;
        }
      }
    }
  }
}
