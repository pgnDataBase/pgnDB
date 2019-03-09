package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;

import java.util.UUID;

class MoveBuilder {

  private Move move;

  public MoveBuilder create() {
    this.move = new Move();
    return this;
  }

  public Move buildVariantEnd(Integer variantId,Integer entityNumber){
    this.move.setSan("-");
    this.move.setFromField("-");
    this.move.setToField("-");
    this.move.setPieceCode("-");
    this.move.setMoveType(MoveType.VEME.name());
    this.move.setMoveNumber(0);
    this.move.setVariantType("VE");
    this.move.setVariantId(variantId);
    this.move.setPositionId(UUID.randomUUID());
    this.move.setFen("-");
    this.move.setEntityNumber(entityNumber);
    return this.move;
  }

  public Move build(com.engwork.pgndb.pgnparser.entities.Move moveEntity) {

    this.move.setSan(moveEntity.getSan());

    String fromField = FieldUtils.parseToFieldCode(moveEntity.getFromX(), moveEntity.getFromY());
    this.move.setFromField(fromField);

    String toField = FieldUtils.parseToFieldCode(moveEntity.getToX(), moveEntity.getToY());
    this.move.setToField(toField);

    String moveType = MoveTypeParser.parse(moveEntity.isCapture(), moveEntity.isEnPassant(), moveEntity.isKSCastling(), moveEntity.isQSCastling());
    this.move.setMoveType(moveType);


    PieceColor myColor = PieceColor.getByValue(moveEntity.getColor());
    if (myColor == null) {
      myColor = moveEntity.getPiece() >= 256 ? PieceColor.BLACK : PieceColor.WHITE;
    }
    String pieceCode = PieceCodeParser.parse(moveEntity.getPiece(), myColor);
    this.move.setPieceCode(pieceCode);

    if (moveEntity.isCapture() && !moveEntity.isEnPassant()) {
      PieceColor opponentColor = PieceColor.getOpposite(myColor);
      String capturedPieceCode = PieceCodeParser.parse(moveEntity.getCapturedPiece(), opponentColor);
      this.move.setCapturedPieceCode(capturedPieceCode);
    }

    if (moveEntity.isEnPassant()) {
      PieceColor opColor = PieceColor.getOpposite(myColor);
      String capturedPieceCode = PieceCodeParser.parse(0, opColor);
      this.move.setCapturedPieceCode(capturedPieceCode);
    }

    if (moveEntity.getPromotion() > 0) {
      String promotedPieceCode = PieceCodeParser.parse(moveEntity.getPromotion(), myColor);
      this.move.setPromotedPieceCode(promotedPieceCode);
    }

    this.move.setMoveNumber(moveEntity.getNo());

    return this.move;

  }

  private static class MoveTypeParser {
    private static String parse(boolean isCapture, boolean isEnPassant, boolean isKSCastling, boolean isQSCastling) {
      if (isEnPassant) {
        return MoveType.ENPT.name();
      }
      if (isCapture) {
        return MoveType.ATCK.name();
      }
      if (isKSCastling) {
        return MoveType.KSCG.name();
      }
      if (isQSCastling) {
        return MoveType.QSCG.name();
      }
      return MoveType.MOVE.name();
    }
  }

  private static class PieceCodeParser {
    private static PieceColor detectPieceColor(int piece) {
      if (piece >= PieceColor.BLACK.value) {
        return PieceColor.BLACK;
      } else if (piece >= PieceColor.WHITE.value) {
        return PieceColor.WHITE;
      }
      return null;
    }

    private static Piece detectPiece(int pieceValue) {
      if (pieceValue >= PieceColor.BLACK.value) {
        return Piece.getByValue(pieceValue - PieceColor.BLACK.value);
      } else if (pieceValue >= PieceColor.WHITE.value) {
        return Piece.getByValue(pieceValue - PieceColor.WHITE.value);
      }
      return Piece.getByValue(pieceValue);
    }

    private static String parse(int pieceValue, PieceColor pieceColor) {
      String pieceCode = "";

      PieceColor detectedColor = detectPieceColor(pieceValue);
      if (detectedColor != pieceColor && detectedColor != null) {
        pieceColor = detectedColor;
      }

      if (pieceColor != null) {
        pieceCode += pieceColor.getPieceColorCode();
      }

      Piece piece = detectPiece(pieceValue);

      if (piece != null) {
        pieceCode += piece.getPieceCode();
      }

      return pieceCode;
    }
  }


}
