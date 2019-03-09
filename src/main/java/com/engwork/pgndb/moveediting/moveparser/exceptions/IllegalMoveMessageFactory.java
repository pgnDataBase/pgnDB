package com.engwork.pgndb.moveediting.moveparser.exceptions;

public class IllegalMoveMessageFactory {
  private static final String NOT_YOUR_TURN_MESSAGE = "You should move pieces in opposite color!";
  private static final String ILLEGAL_MOVE_MESSAGE = "This move is illegal! Try another!";
  private static final String ILLEGAL_EN_PASSANT_MESSAGE = "You can't do en passant move!";
  private static final String CASTLING_NOT_POSSIBLE = "You can't do castling!";

  public static String notYourTurn(){
    return NOT_YOUR_TURN_MESSAGE;
  }

  public static String illegalMove(){
    return ILLEGAL_MOVE_MESSAGE;
  }

  public static String illegalEnPassantMove(){
    return ILLEGAL_EN_PASSANT_MESSAGE;
  }

  public static String castlingNotAllowed(){
    return CASTLING_NOT_POSSIBLE;
  }
}
