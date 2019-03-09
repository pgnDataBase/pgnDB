package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.Piece;
import com.engwork.pgndb.chessgamesconverter.model.PieceColor;
import com.engwork.pgndb.chessgamesconverter.model.Position;
import java.util.UUID;
import static java.lang.Math.toIntExact;

import com.engwork.pgndb.pgnparser.entities.Move;
import org.springframework.util.StringUtils;

public class PositionBuilder {
  private Position position;

  public PositionBuilder create() {
    this.position = new Position();
    return this;
  }

  public Position buildStartPosition() {
    this.position.setId(UUID.randomUUID());
    this.position.setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    this.position.setBlackPiecesCount(16);
    this.position.setBlackPawnsCount(8);
    this.position.setWhitePawnsCount(8);
    this.position.setWhitePiecesCount(16);
    return position;
  }

  public Position buildFromFen(String fen) {
    this.position.setFen(fen);
    String fenStart = fen.split(" ")[0];
    this.position.setBlackPiecesCount(toIntExact(fenStart.chars().filter(c -> c >= 'A' && c <= 'Z').count()));
    this.position.setBlackPawnsCount(StringUtils.countOccurrencesOf("p", fen));
    this.position.setWhitePawnsCount(StringUtils.countOccurrencesOf("P", fen));
    this.position.setWhitePiecesCount(toIntExact(fenStart.chars().filter(c -> c >= 'a' && c <= 'z').count()));
    return position;
  }

  public Position build(Move moveEntity) {
    int[][] board = moveEntity.getFenElements().getBoard();
    int myColor = moveEntity.getColor();
    String castlingInfo = moveEntity.getFenElements().getCastlingInfo();
    Integer halfMoves = moveEntity.getFenElements().getMovesWithoutCapture();
    Integer fullMoves = (moveEntity.getNo()/2)+1;
    String enPassantInfo = createEnPassantInfo(moveEntity);
    String fen = FenExtracter.getFen(board, myColor,castlingInfo,enPassantInfo,halfMoves,fullMoves);
    this.position.setFen(fen);
    this.setPositionsStatistics(board);
    return position;
  }

  private String createEnPassantInfo(Move moveEntity){
    int moveLength = Math.abs(moveEntity.getFromY() - moveEntity.getToY());
    if(Piece.PAWN.getValue().equals(moveEntity.getPiece()) && moveLength==2){
      int xPos = moveEntity.getToX();
      int yPos = (moveEntity.getFromY() + moveEntity.getToY())/2;
      return FieldUtils.parseToFieldCode(xPos,yPos).toLowerCase();
    } else
      return null;
  }

  private void setPositionsStatistics(int[][] board) {
    int whitePawnsCount = 0;
    int blackPawnsCount = 0;
    int whitePiecesCount = 0;
    int blackPiecesCount = 0;
    for (int[] aBoard : board) {
      for (int code : aBoard) {
        if (code != 0) {
          if (code >= PieceColor.BLACK.value) {
            blackPiecesCount++;
            if (code - PieceColor.BLACK.value == 0)
              blackPawnsCount++;
          } else {
            whitePiecesCount++;
            if (code - PieceColor.WHITE.value == 0)
              whitePawnsCount++;
          }
        }
      }
    }
    this.position.setBlackPiecesCount(blackPiecesCount);
    this.position.setBlackPawnsCount(blackPawnsCount);
    this.position.setWhitePawnsCount(whitePawnsCount);
    this.position.setWhitePiecesCount(whitePiecesCount);
  }
}
