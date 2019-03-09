package com.engwork.pgndb.chessengine.utils;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import com.engwork.pgndb.moveediting.moveparser.model.FenParts;
import com.engwork.pgndb.moveediting.moveparser.utils.BoardUtils;


public class PrincipalVariationParser {

  public static String parse(String principalVariation,String fen){
    StringBuilder movesChain= new StringBuilder();
    FenParts fenParts = new FenParts(fen);
    Integer moveNumber = fenParts.getFullMoves();
    boolean moveNoInc = PieceColor.BLACK.value.equals(fenParts.getOnMove());
    String blackFirstMove =moveNumber.toString()+"... ";
    int[][] board = BoardUtils.getBoardFromFen(fen);
    for (String move : principalVariation.split(" ")){
      String san = SanGenerator.parseMove(move,board);
      if(!moveNoInc)
        movesChain.append(String.format("%s. %s ",moveNumber,san));
      else
        movesChain.append(String.format("%s%s ",blackFirstMove,san));
      if(moveNoInc)
        moveNumber++;
      moveNoInc= !moveNoInc;
      blackFirstMove="";
    }
    return movesChain.toString();
  }

}
