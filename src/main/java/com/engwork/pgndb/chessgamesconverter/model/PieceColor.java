package com.engwork.pgndb.chessgamesconverter.model;

import java.util.HashMap;
import java.util.Map;

public enum PieceColor {
  WHITE(128, "W"),
  BLACK(256, "B");

  public final Integer value;
  public final String colorCode;

  PieceColor(Integer value, String colorCode) {
    this.value = value;
    this.colorCode = colorCode;
  }

  public Integer getValue() {
    return value;
  }

  public String getPieceColorCode() {
    return colorCode;
  }

  private static final Map<Integer, PieceColor> map;
  private static final Map<String, PieceColor> colorMap;

  static {
    map = new HashMap<>();
    colorMap = new HashMap<>();
    for (PieceColor pieceColor : PieceColor.values()) {
      map.put(pieceColor.value, pieceColor);
      colorMap.put(pieceColor.colorCode,pieceColor);
    }
  }

  public static PieceColor getByValue(Integer value) {
    return map.getOrDefault(value, null);
  }

  public static PieceColor getByColorCode(String colorCode) {
    return colorMap.getOrDefault(colorCode, null);
  }

  public static PieceColor getOpposite(PieceColor pieceColor) {
    return map.get((pieceColor.value % 256) + 128);
  }
}
