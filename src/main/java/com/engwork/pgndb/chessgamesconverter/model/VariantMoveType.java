package com.engwork.pgndb.chessgamesconverter.model;

public enum VariantMoveType {
  VARIANT_BEGIN("VB"),
  VARIANT_INSIDE("VI"),
  VARIANT_SINGLE_MOVE("VS"),
  VARIANT_END("VE");

  public String value;
  VariantMoveType(String value){
    this.value = value;
  }
}
