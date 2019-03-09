package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.Field;

import java.util.Arrays;
import java.util.List;

public class FieldUtils {
  private static final Character[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

  public static Field parse(String fieldCode) {
    List<Character> letterList = Arrays.asList(letters);
    int xPos = letterList.indexOf(fieldCode.toUpperCase().charAt(0));
    int yPos = Character.getNumericValue(fieldCode.toUpperCase().charAt(1)) -1;
    return new Field(xPos,yPos,fieldCode.toUpperCase());
  }

  public static String parseToFieldCode(int xPos, int yPos) {
    return letters[xPos] + Integer.toString(yPos + 1);
  }
}
