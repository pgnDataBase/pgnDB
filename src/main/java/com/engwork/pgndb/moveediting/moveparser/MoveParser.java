package com.engwork.pgndb.moveediting.moveparser;

import com.engwork.pgndb.chessgamesconverter.FenExtracter;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.MoveType;
import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import com.engwork.pgndb.moveediting.moveparser.exceptions.CheckedKingException;
import com.engwork.pgndb.moveediting.moveparser.exceptions.IllegalMoveException;
import com.engwork.pgndb.moveediting.moveparser.exceptions.IllegalMoveMessageFactory;
import com.engwork.pgndb.moveediting.moveparser.model.DetectedMove;

import com.engwork.pgndb.moveediting.moveparser.model.FenParts;
import com.engwork.pgndb.moveediting.NewMoveRequest;
import com.engwork.pgndb.chessgamesconverter.model.Field;
import com.engwork.pgndb.moveediting.moveparser.moveanalyzer.MoveAnalyzer;
import com.engwork.pgndb.moveediting.moveparser.moveanalyzer.MoveAnalyzerFactory;
import com.engwork.pgndb.moveediting.moveparser.utils.BoardUtils;
import com.engwork.pgndb.moveediting.moveparser.utils.CastlingMoveUtils;
import com.engwork.pgndb.chessgamesconverter.FieldUtils;
import com.engwork.pgndb.moveediting.moveparser.utils.PieceUtils;
import com.engwork.pgndb.moveediting.moveparser.utils.SanProvider;
import com.engwork.pgndb.pgnparser.PossibleMovesProvider;
import static java.lang.Math.abs;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MoveParser {
  private final static String START_FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
  private PossibleMovesProvider possibleMovesProvider;

  public Move parseMove(NewMoveRequest newMoveRequest){
    String startFen = getStartFen(newMoveRequest.getPreviousMove());
    int[][] board = BoardUtils.getBoardFromFen(startFen);
    DetectedMove detectedMove = new DetectedMoveBuilder().create().build(newMoveRequest);
    Integer color = PieceUtils.getPieceColor(detectedMove.getMovedPiece());
    FenParts fenParts = new FenParts(startFen);
    Integer movedPieceNumber =PieceUtils.getPieceNumber(detectedMove.getMovedPiece());
    MoveAnalyzeHandler handler = new MoveAnalyzeHandler(detectedMove,board);
    if(handler.move!=null){
      setMoveInfo(handler.move, newMoveRequest.getPreviousMove(),newMoveRequest.getNewMove());
      validateMove(handler.move,board,color,fenParts);
      managePromotion(handler.move,detectedMove,board,newMoveRequest.getNewMove(),color,movedPieceNumber);
      SanProvider.setSanForMove(handler.move,movedPieceNumber,handler.similarMove);
      CastlingMoveUtils.updateCastlingInfo(board,movedPieceNumber,color,fenParts.getCastlingInfo());
      setFen(handler.move,fenParts,board,color,detectedMove);
      if(handler.move.getMoveType().equals(MoveType.ENPT.name())){
        enPassantValidation(detectedMove,newMoveRequest.getPreviousMove());
      }
    } else {
      throw new IllegalMoveException(IllegalMoveMessageFactory.illegalMove());
    }
    return handler.move;
  }

  private void managePromotion(Move move,DetectedMove detectedMove,int[][] board,Move newMove,Integer color,Integer movedPieceNumber){
    if(movedPieceNumber.equals(Piece.PAWN.getValue()) && (detectedMove.getFromY().equals(0) || detectedMove.getToY().equals(7))){
      String promotedPieceCode = newMove.getPromotedPieceCode();
      if(promotedPieceCode==null){
        move.setPromotionAllowed(true);
      } else if(promotedPieceCode.length()==2) {
        Integer promotedPiece = Piece.getByPieceCode(promotedPieceCode).getValue();
        board[detectedMove.getToX()][detectedMove.getToY()]= promotedPiece + color;
        move.setPromotedPieceCode(PieceColor.getByValue(color).colorCode+promotedPieceCode);
      }
    }
  }

  private void setFen(Move move,FenParts fenParts,int[][] board, Integer color,DetectedMove detectedMove) {
    String castlingInfo = fenParts.getCastlingInfo().toString();
    String enPassantInfo = createEnPassantInfo(detectedMove);
    int halfMoves = fenParts.getHalfMoves()+1;
    Integer fullMoves = (move.getMoveNumber()/2)+1;
    if(move.getCapturedPieceCode()!=null || move.getPieceCode().contains(Piece.PAWN.getPieceCode()))
      halfMoves=0;
    move.setFen(FenExtracter.getFen(board,color,castlingInfo,enPassantInfo,halfMoves,fullMoves));
  }

  private String createEnPassantInfo(DetectedMove detectedMove){
    int moveLength = Math.abs(detectedMove.getFromY() - detectedMove.getToY());
    if(Piece.PAWN.getValue().equals(detectedMove.getMovedPiece()) && moveLength==2){
      int xPos = detectedMove.getToX();
      int yPos = (detectedMove.getFromY() + detectedMove.getToY())/2;
      return FieldUtils.parseToFieldCode(xPos,yPos).toLowerCase();
    } else
      return "-";
  }

  private String getStartFen(Move parentMove){
    if(parentMove!=null)
      return parentMove.getFen();
    else
      return START_FEN;
  }

  private void setMoveInfo(Move move, Move parentMove,Move newMove){
    move.setId(UUID.randomUUID());
    move.setFromField(newMove.getFromField());
    move.setVariantType(newMove.getVariantType());
    move.setToField(newMove.getToField());
    if(parentMove!=null){
      move.setMoveNumber(parentMove.getMoveNumber() + 1);
      move.setGameId(parentMove.getGameId());
      if(parentMove.getVariantId()!=null){
        move.setVariantId(parentMove.getVariantId());
        move.setVariantType("VI");
      }
    } else {
      move.setMoveNumber(1);
    }
  }

  private static class MoveAnalyzeHandler{
    Move move;
    DetectedMove similarMove;
    MoveAnalyzeHandler(DetectedMove detectedMove,int[][] board){
      List<DetectedMove> possibleSimilarMoves = BoardUtils.findSimilarMoves(board,detectedMove);
      Integer movedPieceNumber =PieceUtils.getPieceNumber(detectedMove.getMovedPiece());
      MoveAnalyzer moveAnalyzer = MoveAnalyzerFactory.getMoveAnalyzer(movedPieceNumber);
      similarMove = possibleSimilarMoves.stream().filter(dMove -> moveAnalyzer.analyzeMove(dMove,board.clone())!=null).findFirst().orElse(null);
      move = moveAnalyzer.analyzeMove(detectedMove,board);
      executeMove(detectedMove,board);
    }
    private void executeMove(DetectedMove detectedMove,int[][] board){
      board[detectedMove.getToX()][detectedMove.getToY()]=board[detectedMove.getFromX()][detectedMove.getFromY()];
      board[detectedMove.getFromX()][detectedMove.getFromY()] = 0;
    }
  }

  private void validateMove(Move move,int[][] board,Integer color,FenParts fenParts){
    String moveType = move.getMoveType();
    if((moveType.equals(MoveType.KSCG.name()) || moveType.equals(MoveType.QSCG.name()) ) && !CastlingMoveUtils.isValidCastlingMove(fenParts.getCastlingInfo(),moveType,color))
      throw new IllegalMoveException(IllegalMoveMessageFactory.castlingNotAllowed());
    if(possibleMovesProvider.isKingChecked(board,color))
      throw new CheckedKingException(PieceColor.getByValue(color).name());
    if(!fenParts.getOnMove().equals(color)){
      throw new IllegalMoveException(IllegalMoveMessageFactory.notYourTurn());
    }
  }

  private void enPassantValidation(DetectedMove detectedMove,Move previousMove){
    Field pMFromField  = FieldUtils.parse(previousMove.getFromField());
    Field pMToField  = FieldUtils.parse(previousMove.getToField());
    String pMPieceCode = previousMove.getPieceCode();
    Integer pMPieceNumber = Piece.getByPieceCode(pMPieceCode.substring(1)).getValue();
    boolean pawnWasMoved = Piece.PAWN.getValue().equals(pMPieceNumber);
    boolean wasLongMove = abs(pMFromField.getYPos() - pMToField.getYPos())==2;
    boolean goodEPPosition = pMToField.getYPos().equals(detectedMove.getFromY()) && pMToField.getXPos().equals(detectedMove.getToX());
    if(!pawnWasMoved || !wasLongMove || !goodEPPosition)
      throw new IllegalMoveException(IllegalMoveMessageFactory.illegalEnPassantMove());

  }
}
