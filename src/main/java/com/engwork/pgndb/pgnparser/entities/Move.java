package com.engwork.pgndb.pgnparser.entities;

import com.engwork.pgndb.pgnparser.FenElements;
import com.engwork.pgndb.pgnparser.SANHelper;
import com.engwork.pgndb.pgnparser.consts.Piece;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Stanisław Kabaciński.
 */

@Data
@NoArgsConstructor
public class Move extends Entity {

  private int no;
  private int fromX;
  private int fromY;
  private int toX;
  private int toY;
  private boolean isQSCastling = false;
  private boolean isKSCastling = false;
  private boolean enPassant = false;
  private int promotion = 0;
  private int capturedPiece = -1;
  private int piece = 0;
  private int color = 0;
  private int[] nag = {};
  private FenElements fenElements;
  private String san;


  public boolean isCastling() {
    return isKSCastling || isQSCastling;
  }

  public boolean isCapture() {
    return this.capturedPiece != -1;
  }

  public String getPANRepresentation() {
    String move = SANHelper.getField(fromX, fromY) + SANHelper.getField(toX, toY);
    switch (promotion) {
      case Piece.QUEEN:
        move += 'q';
        break;
      case Piece.BISHOP:
        move += 'b';
        break;
      case Piece.KNIGHT:
        move += 'n';
        break;
      case Piece.QS_ROOK:
      case Piece.KS_ROOK:
        move += 'r';
        break;
      default:
        break;
    }
    return move;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Move)) {
      return false;
    }

    Move m = ((Move) obj);
    return no == m.no
        && fromX == m.fromX && fromY == m.fromY
        && toX == m.toX && toY == m.toY
        && promotion == m.promotion;
  }
}
