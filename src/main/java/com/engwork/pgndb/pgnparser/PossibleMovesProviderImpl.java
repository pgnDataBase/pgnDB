package com.engwork.pgndb.pgnparser;

import com.engwork.pgndb.pgnparser.consts.Consts;
import com.engwork.pgndb.pgnparser.consts.Piece;
import com.engwork.pgndb.pgnparser.entities.Move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stanisław Kabaciński.
 */
public class PossibleMovesProviderImpl implements PossibleMovesProvider, Consts {
  public static final String TAG = "PossibleMovesProvider";

  private static final int[] KNIGHT_MOVES = {
      1, 2, 2, 1, -1, 2, 2, -1,
      1, -2, -2, 1, -1, -2, -2, -1
  };

  private BoardHelper bh = new BoardHelper();
  private HashMap<Integer, GetMoves> funcMap = new HashMap<>();
  private ParsedChessGame game;

  private void initFunctions(){
    funcMap.put(WHITE_PAWN, this::pawnMoves);
    funcMap.put(BLACK_PAWN, this::pawnMoves);
    funcMap.put(WHITE_KING, this::kingMoves);
    funcMap.put(BLACK_KING, this::kingMoves);
    funcMap.put(WHITE_QS_ROOK, this::rookMoves);
    funcMap.put(WHITE_KS_ROOK, this::rookMoves);
    funcMap.put(BLACK_QS_ROOK, this::rookMoves);
    funcMap.put(BLACK_KS_ROOK, this::rookMoves);
    funcMap.put(WHITE_BISHOP, this::bishopMoves);
    funcMap.put(BLACK_BISHOP, this::bishopMoves);
    funcMap.put(WHITE_QUEEN, this::queenMoves);
    funcMap.put(BLACK_QUEEN, this::queenMoves);
    funcMap.put(WHITE_KNIGHT, this::knightMoves);
    funcMap.put(BLACK_KNIGHT, this::knightMoves);
  }
  public PossibleMovesProviderImpl(ParsedChessGame game) {
    this.game = game;
    initFunctions();
  }
  public PossibleMovesProviderImpl() {
    this.game = new ParsedChessGameImpl();
    initFunctions();
  }

  @Override
  public List<Integer> getPossibleMoves(int pieceWithColor, int posX, int posY) {
    if ((pieceWithColor & (BLACK | WHITE)) != game.getNextMovePlayerColor()) {
      return new ArrayList<>();
    }
    bh.setBoard(game.getBoard());
    List<Integer> list = getDirectMoves(pieceWithColor, posX, posY);
    removeKingThreateningMoves(posX, posY, pieceWithColor & (BLACK | WHITE), list);
    castlingMoves(pieceWithColor, posX, posY, list);
    return list;
  }

  private void castlingMoves(int piece, int posX, int posY, List<Integer> list) {
    bh.setBoard(game.getBoard());
    if (bh.isPiece(posX, posY, Piece.KING)) {
      int color = piece & (BLACK | WHITE);
      List<Integer> otherMoves = getAllMoves(BoardHelper.getOppositeColor(color));
      //castling
      if (!game.wasKingMoved(color) && !containsField(posX, posY, list)) {
        if (!game.wasKSRookMoved(color)) {
          if (color == BLACK) {
            if (bh.isEmpty(5, 7) && bh.isEmpty(6, 7)
                && !containsField(5, 7, otherMoves)
                && !containsField(6, 7, otherMoves)
                && !containsField(4, 7, otherMoves)) {
              list.add(6);
              list.add(7);
            }
          } else {
            if (bh.isEmpty(5, 0) && bh.isEmpty(6, 0)
                && !containsField(5, 0, otherMoves)
                && !containsField(6, 0, otherMoves)
                && !containsField(4, 0, otherMoves)) {
              list.add(6);
              list.add(0);
            }
          }
        }
        if (!game.wasQSRookMoved(color)) {
          if (color == BLACK) {
            if (bh.isEmpty(1, 7) && bh.isEmpty(2, 7) && bh.isEmpty(3, 7)
                && !containsField(1, 7, otherMoves)
                && !containsField(2, 7, otherMoves)
                && !containsField(3, 7, otherMoves)
                && !containsField(4, 7, otherMoves)) {
              list.add(2);
              list.add(7);
            }
          } else {
            if (bh.isEmpty(1, 0) && bh.isEmpty(2, 0) && bh.isEmpty(3, 0)
                && !containsField(1, 0, otherMoves)
                && !containsField(2, 0, otherMoves)
                && !containsField(3, 0, otherMoves)
                && !containsField(4, 0, otherMoves)) {
              list.add(2);
              list.add(0);
            }
          }
        }
      }
    }
  }

  private List<Integer> getDirectMoves(int piece, int posX, int posY) {
    int color = piece & (BLACK | WHITE);
    List<Integer> list = funcMap.get(piece).getMoves(posX, posY, color);
    if (list == null) return new ArrayList<>();
    return list;
  }

  private void removeKingThreateningMoves(int posX, int posY, int color, List<Integer> list) {
    funcMap.remove(WHITE_PAWN);
    funcMap.remove(BLACK_PAWN);
    funcMap.put(WHITE_PAWN, this::pawnAttackMoves);
    funcMap.put(BLACK_PAWN, this::pawnAttackMoves);

    List<Integer> toRemove = new ArrayList<>();
    for (int i = 0; i < list.size() / 2; ++i) {
      int x = list.get(i * 2);
      int y = list.get(i * 2 + 1);
      if (compromisesKing(posX, posY, x, y, color)) {
        toRemove.add(i * 2);
      }
    }
    for (int i = toRemove.size() - 1; i >= 0; --i) {
      int pos = toRemove.get(i);
      list.remove(pos + 1);
      list.remove(pos);
    }

    funcMap.remove(WHITE_PAWN);
    funcMap.remove(BLACK_PAWN);
    funcMap.put(WHITE_PAWN, this::pawnMoves);
    funcMap.put(BLACK_PAWN, this::pawnMoves);
  }

  private boolean compromisesKing(int fromX, int fromY, int toX, int toY, int color) {
    int[][] newBoard = BoardHelper.copyBoard(game.getBoard());
    if(newBoard[fromX][fromY] == color + Piece.PAWN && fromX != toX && newBoard[toX][toY] == 0){
      newBoard[toX][fromY]=0;
    }
    newBoard[toX][toY] = newBoard[fromX][fromY];
    newBoard[fromX][fromY] = 0;
    for (int x = 0; x <= 7; x++) {
      for (int y = 0; y <= 7; y++) {
        if (newBoard[x][y] == color + Piece.KING) {
          return !isFieldSafeForKing(newBoard, x, y, color);
        }
      }
    }
    return false;
  }

  @Override
  public boolean isKingChecked(int[][] board, int color) {
    for (int x = 0; x <= 7; x++) {
      for (int y = 0; y <= 7; y++) {
        if (board[x][y] == color + Piece.KING) {
          return !isFieldSafeForKing(board, x, y, color);
        }
      }
    }
    return false;
  }

  private boolean isFieldSafeForKing(int[][] board, int x, int y, int color) {
    bh.setBoard(board);
    for (int bx = 0; bx <= 7; ++bx) {
      for (int by = 0; by <= 7; ++by) {
        if (board[bx][by] == 0 || bh.isColor(bx, by, color)) continue;
        List<Integer> list = getDirectMoves(board[bx][by], bx, by);
        for (int i = 0; i < list.size() / 2; i++) {
          int tx = list.get(2 * i);
          int ty = list.get(2 * i + 1);
          if (tx == x && ty == y) return false;
        }
      }
    }
    return true;
  }

  private List<Integer> getAllMoves(int color) {
    List<Integer> list = new ArrayList<>();
    for (int bx = 0; bx <= 7; ++bx) {
      for (int by = 0; by <= 7; ++by) {
        if (bh.getBoard()[bx][by] == 0 || !bh.isColor(bx, by, color)) continue;
        list.addAll(getDirectMoves(bh.getBoard()[bx][by], bx, by));
      }
    }
    return list;
  }

  private List<Integer> pawnMoves(int x, int y, int color) {
    List<Integer> list = new ArrayList<>();
    int opColor = BoardHelper.getOppositeColor(color);
    int m = color == WHITE ? 1 : -1; // unification of white and black pawn moves
    if (!bh.inRange(y + m)) return list;

    if (bh.isColor(x - 1, y + m, opColor)) {
      list.add(x - 1);
      list.add(y + m);
    }
    if (bh.isColor(x + 1, y + m, opColor)) {
      list.add(x + 1);
      list.add(y + m);
    }

    // e.p.
    List<Move> movesList = game.getMoveList();
    if (movesList.size() != 0) {
      Move lastMove = game.getMoveList().get(movesList.size() - 1);
      if (lastMove.getPiece() == PAWN
          && Math.abs(lastMove.getFromY() - lastMove.getToY()) == 2
          && lastMove.getToY() == y) {
        if (x + 1 == lastMove.getToX()
            && bh.isColor(x + 1, y, opColor)
            && bh.isPiece(x + 1, y, Piece.PAWN)
            && bh.isEmpty(x + 1, y + m)) {
          list.add(x + 1);
          list.add(y + m);
        }
        if (x - 1 == lastMove.getToX()
            && bh.isColor(x - 1, y, opColor)
            && bh.isPiece(x - 1, y, Piece.PAWN)
            && bh.isEmpty(x - 1, y + m)) {
          list.add(x - 1);
          list.add(y + m);
        }
      }
    }

    if (bh.isEmpty(x, y + m)) {
      list.add(x);
      list.add(y + m);
    }
    if ((color == WHITE && y == 1) || (color == BLACK && y == 6)) {
      if (bh.isEmpty(x, y + 2 * m) && bh.isEmpty(x, y + m)) {
        list.add(x);
        list.add(y + 2 * m);
      }
    }
    return list;
  }

  private List<Integer> pawnAttackMoves(int x, int y, int color) {
    List<Integer> list = new ArrayList<>();
    int opColor = BoardHelper.getOppositeColor(color);
    int m = color == WHITE ? 1 : -1; // unification of white and black pawn moves
    if (!bh.inRange(y + m)) return list;

    if (bh.isColor(x - 1, y + m, opColor)) {
      list.add(x - 1);
      list.add(y + m);
    }
    if (bh.isColor(x + 1, y + m, opColor)) {
      list.add(x + 1);
      list.add(y + m);
    }

    return list;
  }

  private List<Integer> kingMoves(int x, int y, int color) {
    List<Integer> list = new ArrayList<>();
    int opColor = BoardHelper.getOppositeColor(color);
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i == 0 && j == 0) continue;
        if (bh.isEmpty(x + i, y + j) || bh.isColor(x + i, y + j, opColor)) {
          list.add(x + i);
          list.add(y + j);
        }
      }
    }
    return list;
  }

  private List<Integer> rookMoves(int x, int y, int color) {
    List<Integer> list = new ArrayList<>();
    int opColor = BoardHelper.getOppositeColor(color);
    for (int i = 1; i <= 7; i++) {
      if (bh.isEmpty(x + i, y)) {
        list.add(x + i);
        list.add(y);
      }
      if (bh.isColor(x + i, y, opColor)) {
        list.add(x + i);
        list.add(y);
        break;
      }
      if (bh.isColor(x + i, y, color)) break;
    }
    for (int i = 1; i <= 7; i++) {
      if (bh.isEmpty(x - i, y)) {
        list.add(x - i);
        list.add(y);
      }
      if (bh.isColor(x - i, y, opColor)) {
        list.add(x - i);
        list.add(y);
        break;
      }
      if (bh.isColor(x - i, y, color)) break;
    }
    for (int i = 1; i <= 7; i++) {
      if (bh.isEmpty(x, y + i)) {
        list.add(x);
        list.add(y + i);
      }
      if (bh.isColor(x, y + i, opColor)) {
        list.add(x);
        list.add(y + i);
        break;
      }
      if (bh.isColor(x, y + i, color)) break;
    }
    for (int i = 1; i <= 7; i++) {
      if (bh.isEmpty(x, y - i)) {
        list.add(x);
        list.add(y - i);
      }
      if (bh.isColor(x, y - i, opColor)) {
        list.add(x);
        list.add(y - i);
        break;
      }
      if (bh.isColor(x, y - i, color)) break;
    }
    return list;
  }

  private List<Integer> bishopMoves(int x, int y, int color) {
    List<Integer> list = new ArrayList<>();
    int opColor = BoardHelper.getOppositeColor(color);
    for (int i = 1; i <= 7; i++) {
      if (bh.isEmpty(x + i, y + i)) {
        list.add(x + i);
        list.add(y + i);
      }
      if (bh.isColor(x + i, y + i, opColor)) {
        list.add(x + i);
        list.add(y + i);
        break;
      }
      if (bh.isColor(x + i, y + i, color)) break;
    }
    for (int i = 1; i <= 7; i++) {
      if (bh.isEmpty(x - i, y - i)) {
        list.add(x - i);
        list.add(y - i);
      }
      if (bh.isColor(x - i, y - i, opColor)) {
        list.add(x - i);
        list.add(y - i);
        break;
      }
      if (bh.isColor(x - i, y - i, color)) break;
    }
    for (int i = 1; i <= 7; i++) {
      if (bh.isEmpty(x - i, y + i)) {
        list.add(x - i);
        list.add(y + i);
      }
      if (bh.isColor(x - i, y + i, opColor)) {
        list.add(x - i);
        list.add(y + i);
        break;
      }
      if (bh.isColor(x - i, y + i, color)) break;
    }
    for (int i = 1; i <= 7; i++) {
      if (bh.isEmpty(x + i, y - i)) {
        list.add(x + i);
        list.add(y - i);
      }
      if (bh.isColor(x + i, y - i, opColor)) {
        list.add(x + i);
        list.add(y - i);
        break;
      }
      if (bh.isColor(x + i, y - i, color)) break;
    }
    return list;
  }

  private List<Integer> queenMoves(int x, int y, int color) {
    List<Integer> list = new ArrayList<>();
    list.addAll(rookMoves(x, y, color));
    list.addAll(bishopMoves(x, y, color));
    return list;
  }

  private List<Integer> knightMoves(int x, int y, int color) {
    List<Integer> list = new ArrayList<>();
    color = BoardHelper.getOppositeColor(color);
    for (int i = 0, ii = KNIGHT_MOVES.length / 2; i < ii; i++) {
      int mx = KNIGHT_MOVES[i * 2];
      int my = KNIGHT_MOVES[i * 2 + 1];
      if (bh.isEmpty(x + mx, y + my) || bh.isColor(x + mx, y + my, color)) {
        list.add(x + mx);
        list.add(y + my);
      }
    }
    return list;
  }

  private boolean containsField(int x, int y, List<Integer> list) {
    for (int i = 0; i < list.size() / 2; i++) {
      int lx = list.get(i * 2);
      int ly = list.get(i * 2 + 1);
      if (lx == x && ly == y) return true;
    }
    return false;
  }

  @Override
  public void setBoard(int[][] board) {
    this.bh.setBoard(board);
  }

  interface GetMoves {
    List<Integer> getMoves(int x, int y, int color);
  }
}
