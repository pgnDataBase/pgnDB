package com.engwork.pgndb.chessgamesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Field {
  private Integer xPos;
  private Integer yPos;
  private String code;
}
