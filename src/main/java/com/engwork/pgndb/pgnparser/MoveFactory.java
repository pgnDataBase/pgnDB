package com.engwork.pgndb.pgnparser;

import com.engwork.pgndb.pgnparser.consts.Piece;
import com.engwork.pgndb.pgnparser.consts.PieceColor;
import com.engwork.pgndb.pgnparser.consts.SANConsts;
import com.engwork.pgndb.pgnparser.entities.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanisław Kabaciński.
 */

public class MoveFactory implements Piece, PieceColor {
  private PossibleMovesProvider possibleMovesProvider;
  private OnMoveMadeListener onMoveMadeListener;

  public MoveFactory(PossibleMovesProvider possibleMovesProvider) {
    this.possibleMovesProvider = possibleMovesProvider;
  }

  public Move getMove(int[][] board, int fromX, int fromY, int toX, int toY) {
    int piece = board[fromX][fromY];
    int color = piece & (WHITE | BLACK);
    Move move = new Move();
    move.setPiece(piece - color);
    move.setFromX(fromX);
    move.setFromY(fromY);
    move.setColor(color);
    move.setKSCastling(isKSCastling(piece, color, fromX, toX));
    move.setQSCastling(isQSCastling(piece, color, fromX, toX));
    move.setCapturedPiece(board[toX][toY]);
    if (move.isCastling()) {
      if (move.isKSCastling()) {
        move.setToX(6);
        board[6][toY] = piece;
        board[fromX][fromY] = 0;
        board[5][toY] = board[7][toY];
        board[7][toY] = 0;
      } else {
        move.setToX(2);
        board[2][toY] = piece;
        board[fromX][fromY] = 0;
        board[3][toY] = board[0][toY];
        board[0][toY] = 0;
      }
      move.setToY(toY);
      move.setSan(getMoveSANRepresentation(move, board));
    } else {
      move.setCapturedPiece(board[toX][toY]);
      move.setToX(toX);
      move.setToY(toY);
      move.setSan(getMoveSANRepresentation(move, board));
      if (move.getPiece() == PAWN && fromX != toX && board[toX][toY] == 0) {
        move.setCapturedPiece(board[toX][fromY]);
        move.setEnPassant(true);
        board[toX][fromY] = 0;
      }
      if (move.getPiece() == PAWN && (toY == 0 || toY == 7)) {
        move.setPromotion(-1);
        if (onMoveMadeListener != null) {
          onMoveMadeListener.onPromotion(move);
        }
      }
      board[toX][toY] = piece;
      board[fromX][fromY] = 0;
    }
    return move;
  }

  private boolean isQSCastling(int piece, int color, int fx, int tx) {
    return piece - color == KING && tx == 2 && Math.abs(fx - tx) == 2;
  }

  private boolean isKSCastling(int piece, int color, int fx, int tx) {
    return piece - color == KING && tx == 6 && Math.abs(fx - tx) == 2;
  }

  private String getMoveSANRepresentation(Move move, int board[][]) {
    if (move.isKSCastling()) return SANConsts.KS_CASTLING;
    if (move.isQSCastling()) return SANConsts.QS_CASTLING;
    String out = SANHelper.getSymbol(move.getPiece() + move.getColor());
    String field = SANHelper.getField(move.getToX(), move.getToY());

    if (move.getPiece() == KING) {
      return out + (move.isCapture() ? ':' : "") + field;
    }

    out += getDisambiguatingString(move, board);
    if (move.isCapture()) out += ':';
    return out + field;
  }

  private String getDisambiguatingString(Move move, int[][] board) {
    // each consecutive 4 integers in list are in following order [fromX, fromY, toX, toY]
    List<Integer> list = new ArrayList<>();
    for (int x = 0; x < 8; ++x) {
      for (int y = 0; y < 8; ++y) {
        int piece = board[x][y];
        int color = piece & (BLACK | WHITE);
        piece -= color;
        if (color != move.getColor()) continue;
        if (piece != move.getPiece()
            && !((piece == KS_ROOK && move.getPiece() == QS_ROOK)
            || (piece == QS_ROOK && move.getPiece() == KS_ROOK))) continue;
        if (move.getFromX() == x && move.getFromY() == y) continue;
        List<Integer> moves = possibleMovesProvider.getPossibleMoves(board[x][y], x, y);
        for (int k = 0; k < moves.size() / 2; ++k) {
          int toX = moves.get(k * 2);
          int toY = moves.get(k * 2 + 1);
          if (toX == move.getToX() && toY == move.getToY()) {
            list.add(x);
            list.add(y);
            list.add(toX);
            list.add(toY);
          }
        }
      }
    }
    if (move.getPiece() == Piece.PAWN && move.isCapture()) return String.valueOf(SANHelper.getFile(move.getFromX()));
    if (list.size() == 0) return "";
    if (list.size() > 4) return SANHelper.getField(move.getFromX(), move.getFromY());
    if (list.get(0) == move.getFromX()) return String.valueOf(move.getFromY());
    return String.valueOf(SANHelper.getFile(move.getFromX()));
  }

  public void setOnMoveMadeListener(OnMoveMadeListener onMoveMadeListener) {
    this.onMoveMadeListener = onMoveMadeListener;
  }

  public interface OnMoveMadeListener {
    void onPromotion(Move move);
  }
}
