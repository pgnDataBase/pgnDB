package com.engwork.pgndb.chessgamesconverter.model;

import java.util.HashMap;
import java.util.Map;

public enum Piece {
  PAWN(0, "PN",'p',""),
  QS_ROOK(1, "RK",'r',"R"),
  KS_ROOK(2, "RK",'r',"R"),
  KNIGHT(3, "KT",'n',"N"),
  BISHOP(4, "BP",'b',"B"),
  QUEEN(5, "QN",'q',"Q"),
  KING(6, "KG",'k',"K");

  private final Integer value;
  private final String pieceCode;
  private final Character fenCode;
  private final String sanCode;

  Piece(Integer value, String pieceCode,Character fenCode,String sanCode) {
    this.value = value;
    this.pieceCode = pieceCode;
    this.fenCode = fenCode;
    this.sanCode = sanCode;
  }

  public Integer getValue() {
    return value;
  }

  public String getPieceCode() {
    return pieceCode;
  }

  public Character getFenCode() {
    return fenCode;
  }

  public String getSanCode() {
    return sanCode;
  }

  private static final Map<Integer, Piece> map;
  private static final Map<String, Piece> pieceCodeMap;
  private static final Map<Character, Piece> fenCodeMap;

  static {
    map = new HashMap<>();
    pieceCodeMap = new HashMap<>();
    fenCodeMap = new HashMap<>();
    for (Piece piece : Piece.values()) {
      map.put(piece.value, piece);
      pieceCodeMap.put(piece.pieceCode,piece);
      fenCodeMap.put(piece.fenCode,piece);
    }
  }
  public static Piece getByValue(Integer value) {
    return map.getOrDefault(value, null);
  }
  public static Piece getByPieceCode(String  pieceCode) {
    return pieceCodeMap.getOrDefault(pieceCode, null);
  }
  public static Piece getByFenCode(Character fenCode) {
    return fenCodeMap.getOrDefault(fenCode, null);
  }
}
