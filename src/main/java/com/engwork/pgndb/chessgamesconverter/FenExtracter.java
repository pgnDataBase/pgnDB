package com.engwork.pgndb.chessgamesconverter;


import com.engwork.pgndb.chessgamesconverter.model.PieceColor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FenExtracter {

  private static String WHITE_ON_MOVE = " w";
  private static String BLACK_ON_MOVE = " b";
  private static String ROW_END = "/";

  /**
   * Converts board into FEN represenation
   */
  public static String getFen(int[][] board, int activeColor,String castlingInfo,String enPassantInfo,Integer halfMoves,Integer fullMoves) {
    StringBuilder fen = new StringBuilder();
    //Stores number of empty fields between pieces in row
    AtomicInteger emptyFieldsCount = new AtomicInteger(0);
    for (int col = 7; col >= 0; col--) {
      for (int row = 0; row < board[col].length; row++) {
        processField(fen, emptyFieldsCount, board[row][col]);
      }
      addInfoAboutEmptyFields(fen, emptyFieldsCount);
      //If it is not last processed row add
      if (col != 0) {
        emptyFieldsCount.set(0);
        fen.append(ROW_END);
      }
    }
    if(enPassantInfo==null)
      enPassantInfo="-";
    if(halfMoves==null)
      halfMoves=0;
    if (fullMoves==null)
      fullMoves=1;
    fen.append(getActiveColorCode(activeColor)).append(" ").append(castlingInfo)
    .append(" ").append(enPassantInfo).append(" ").append(halfMoves).append(" ")
    .append(fullMoves);
    return fen.toString();
  }

  private static void processField(StringBuilder fen, AtomicInteger emptyFieldsCount, int fieldCode) {
    String pieceFENCode = getFENPieceCode(fieldCode);
    //If on this field stands any piece do the following
    if (pieceFENCode != null) {
      addInfoAboutEmptyFields(fen, emptyFieldsCount);
      //Insert pieceFENCode into fen string
      fen.append(pieceFENCode);
    } else {
      //Else increase emptyFieldsCount
      emptyFieldsCount.incrementAndGet();
    }
  }

  // If previous field or fields in row was empty, insert information about that into FEN
  private static void addInfoAboutEmptyFields(StringBuilder fen, AtomicInteger emptyFieldsCount) {
    if (emptyFieldsCount.get() != 0) {
      fen.append(Integer.toString(emptyFieldsCount.get()));
      emptyFieldsCount.set(0);
    }
  }


  // Get code of color which will have  possibility to move in next turn
  private static String getActiveColorCode(int activeColor) {
    if (activeColor == PieceColor.WHITE.value) {
      return BLACK_ON_MOVE;
    } else {
      return WHITE_ON_MOVE;
    }
  }


  // Get piece code (in FEN representation) of piece which stands on this field. If field is empty it has null value
  private static String getFENPieceCode(int fieldCode) {
    if (fieldCode != 0) {
      if (fieldCode >= PieceColor.BLACK.value) {
        return FENPiece.getByValue(fieldCode - PieceColor.BLACK.value).pieceCode.toLowerCase();
      } else {
        return FENPiece.getByValue(fieldCode - PieceColor.WHITE.value).pieceCode.toUpperCase();
      }
    } else {
      return null;
    }
  }

  private enum FENPiece {
    PAWN(0, "p"),
    QS_ROOK(1, "r"),
    KS_ROOK(2, "r"),
    KNIGHT(3, "n"),
    BISHOP(4, "b"),
    QUEEN(5, "q"),
    KING(6, "k");

    public final Integer value;
    public final String pieceCode;

    FENPiece(Integer value, String pieceCode) {
      this.value = value;
      this.pieceCode = pieceCode;
    }

    private static final Map<Integer, FENPiece> map;

    static {
      map = new HashMap<>();
      for (FENPiece fenPiece : FENPiece.values()) {
        map.put(fenPiece.value, fenPiece);
      }
    }

    public static FENPiece getByValue(Integer value) {
      return map.getOrDefault(value, null);
    }
  }
}
