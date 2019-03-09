package com.engwork.pgndb.moveediting.moveparser.model;

import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import lombok.Data;

@Data
public class FenParts {
  private StringBuilder castlingInfo;
  private String enPassantInfo;
  private Integer halfMoves;
  private Integer fullMoves;
  private Integer onMove;
  public FenParts(String fen){
    castlingInfo=new StringBuilder();
    String[] fenParts = fen.split(" ");
    setOnMove(fenParts[1]);
    castlingInfo.append(fenParts[2]);
    enPassantInfo = fenParts[3];
    halfMoves = Integer.parseInt(fenParts[4]);
    fullMoves = Integer.parseInt(fenParts[5]);
  }
  private void setOnMove(String onMovePiece){
    if(onMovePiece.contains("w"))
      onMove = PieceColor.WHITE.value;
    else
      onMove = PieceColor.BLACK.value;
  }
}