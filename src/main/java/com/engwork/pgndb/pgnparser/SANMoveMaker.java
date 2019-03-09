package com.engwork.pgndb.pgnparser;


//import android.util.Log;

import com.engwork.pgndb.pgnparser.consts.Piece;
import com.engwork.pgndb.pgnparser.consts.PieceColor;
import com.engwork.pgndb.pgnparser.entities.Entity;
import com.engwork.pgndb.pgnparser.entities.Move;
import com.engwork.pgndb.pgnparser.entities.VariantBegin;
import com.engwork.pgndb.pgnparser.entities.VariantEnd;
import com.engwork.pgndb.pgnparser.exceptions.WrongMoveException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stanisław Kabaciński.
 */
@AllArgsConstructor
public class SANMoveMaker {
  public static final String TAG = SANMoveMaker.class.getSimpleName();

  public static final byte TYPE_KINGSIDE_CASTLING = 1;
  public static final byte TYPE_QUEENSIDE_CASTLING = 2;
  public static final byte TYPE_CAPTURE = 4;
  public static final byte TYPE_PROMOTION = 8;
  public static final byte TYPE_PIECE_MOVED = 16;
  public static final byte TYPE_NON_CASTLING = TYPE_CAPTURE | TYPE_PROMOTION | TYPE_PIECE_MOVED;
  private static final Pattern MOVE_PATTERN = Pattern.compile("^([KQNRB]?)([a-h]?[0-8]?)?([x:]?)([a-h]?[0-8]?)(=?)([KQNRB]?)[+#]*");
  private static final Pattern PATTERN_CASTLING_KS = Pattern.compile("^O-O[+]?");
  private static final Pattern PATTERN_CASTLING_QS = Pattern.compile("^O-O-O[+]?");

  private ParsedChessGame parsedChessGame;
  private PossibleMovesProvider possibleMovesProvider;

  public void processMoves(List<Entity> entities) throws WrongMoveException {
    int movesWithoutCapture=0;
    for (int i = 0; i < entities.size(); i++) {
      Entity e = entities.get(i);
      if (e instanceof Move) {
        Move m = (Move) e;
        long time = System.currentTimeMillis();
        try {
          processMove(m, parsedChessGame.getNextMovePlayerColor());
        } catch(NullPointerException exception){
          throw new WrongMoveException(m.getSan());
        }
        parsedChessGame.makeMove(m, m.getFromX(), m.getFromY(), m.getToX(), m.getToY());
        if(!m.isCapture() && m.getPiece()!= Piece.PAWN)
          movesWithoutCapture++;
        else
          movesWithoutCapture=0;
        FenElements fenElements = new FenElements();
        fenElements.setMovesWithoutCapture(movesWithoutCapture);
        fenElements.boardDeepCopy(parsedChessGame.getBoard());
        fenElements.setCastlingInfo(parsedChessGame.getCastlingInfo());
        m.setFenElements(fenElements);
        //Log.d(TAG, String.format("playToPosition time %.10f", (System.currentTimeMillis() - time) / 1000.f));
      } else if (e instanceof VariantBegin || e instanceof VariantEnd) {
        long time = System.currentTimeMillis();
        playToPosition(i, entities);
        //Log.d(TAG, String.format("playToPosition time %.10f", (System.currentTimeMillis() - time) / 1000.f));
      }
    }
  }

  public void processMove(Move m, int color) {
    //Log.e("SANMoveMaker", m.getSan());
    if (m.getSan().equals("*")
        || m.getSan().equals("1-0")
        || m.getSan().equals("0-1")
        || m.getSan().equals("1/2-1/2")
        || m.getSan().equals("Z0")) {
      return;
    }
    m.setColor(color);
    Field from = null;
    Field to = null;
    int promotion = 0;
    int piece = 0;
    int type = 0;

    String san = m.getSan();
    if (PATTERN_CASTLING_KS.matcher(san).matches()) {
      m.setKSCastling(true);
      m.setFromY(color == PieceColor.WHITE ? 0 : 7);
      m.setToY(color == PieceColor.WHITE ? 0 : 7);
      m.setFromX(4);
      m.setToX(6);
      m.setPiece(color +Piece.KING);
      return;
    }
    if (PATTERN_CASTLING_QS.matcher(san).matches()) {
      m.setQSCastling(true);
      m.setFromY(color == PieceColor.WHITE ? 0 : 7);
      m.setToY(color == PieceColor.WHITE ? 0 : 7);
      m.setFromX(4);
      m.setToX(2);
      m.setPiece(color +Piece.KING);
      return;
    }

    Matcher matcher = MOVE_PATTERN.matcher(san);
    if (matcher.matches()) {
      String g = matcher.group(1);
      if (g != null && g.length() == 1) piece = SANHelper.getPieceFromChar(g.charAt(0));
      if (piece == 0) piece = Piece.PAWN;
      if ((g = matcher.group(2)) != null && g.length() != 0) from = parseField(g);
      if ((g = matcher.group(3)) != null && g.length() != 0) type |= TYPE_CAPTURE;
      if ((g = matcher.group(4)) != null && g.length() != 0) to = parseField(g);
      if ((g = matcher.group(6)) != null && g.length() == 1) {
        type |= TYPE_PROMOTION;
        promotion = SANHelper.getPieceFromChar(g.charAt(0));
      }
    }

    m.setPiece(piece);
    m.setPromotion(promotion);
    if (to == null) {
      to = from;
      from = new Field(-1, -1);
    }
    if (from == null) {
      from = new Field(-1, -1);
    }
    m.setToX(to.x);
    m.setToY(to.y);
    from = getFromField(from, to, piece, color);
    m.setFromX(from.x);
    m.setFromY(from.y);

    if ((type & TYPE_CAPTURE) != 0) {
      if (piece == Piece.PAWN && from.x != to.x && parsedChessGame.getBoard()[to.x][to.x] == 0) {
        m.setEnPassant(true);
        m.setCapturedPiece(parsedChessGame.getBoard()[to.x][from.y]);
      } else {
        m.setCapturedPiece(parsedChessGame.getBoard()[to.x][to.y]);
      }
    }
    if ((type & TYPE_PROMOTION) != 0) {
      parsedChessGame.getBoard()[m.getToX()][m.getToY()] = m.getPromotion() + m.getColor();
      parsedChessGame.getBoard()[m.getFromX()][m.getFromY()] = 0;
    }
  }

  private Field getFromField(Field from, Field to, int piece, int color) {
    List<Field> possibleFields = getPossiblePositions(piece, color, from);
    for (Field f : possibleFields) {
      List<Integer> moves = possibleMovesProvider.getPossibleMoves(piece + color, f.x, f.y);
      if (piece == Piece.KS_ROOK) {
        moves.addAll(possibleMovesProvider.getPossibleMoves(Piece.QS_ROOK + PieceColor.BLACK, f.x, f.y));
      } else if (piece == Piece.QS_ROOK) {
        moves.addAll(possibleMovesProvider.getPossibleMoves(Piece.KS_ROOK + PieceColor.BLACK, f.x, f.y));
      }
      for (int i = 0; i < moves.size() / 2; i++) {
        if (to.x == moves.get(i * 2) && to.y == moves.get(i * 2 + 1)) {
          return f;
        }
      }
    }
    return null;
  }

  private List<Field> getPossiblePositions(int piece, int color, Field from) {
    List<Field> list = new ArrayList<>();
    int[][] board = parsedChessGame.getBoard();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if ((from.x != -1 && from.x != i) || (from.y != -1 && from.y != j)) {
          continue;
        }

        if (board[i][j] == piece + color) list.add(new Field(i, j));
        if (piece == Piece.QS_ROOK && board[i][j] == Piece.KS_ROOK + color) {
          list.add(new Field(i, j));
        } else if (piece == Piece.KS_ROOK && board[i][j] == Piece.QS_ROOK + color) {
          list.add(new Field(i, j));
        }
      }
    }
    return list;
  }

  private Field parseField(String field) {
    Field f = new Field();
    if (field.length() == 2) {
      f.x = field.charAt(0) - 'a';
      f.y = Integer.parseInt(field.substring(1)) - 1;
    } else {
      try {
        f.x = -1;
        f.y = Integer.parseInt(field) - 1;
        return f;
      } catch (NumberFormatException e) {
        f.y = -1;
        f.x = field.charAt(0) - 'a';
      }
    }
    return f;
  }

  private int playToPosition(int position, List<Entity> entities) {
    parsedChessGame.reset();
    List<Move> newMoves = MovesFilter.filter(entities, position);
    for (Move m : newMoves) {
      parsedChessGame.makeMove(m, m.getFromX(), m.getFromY(), m.getToX(), m.getToY());
    }
    return newMoves.size();
  }

  private class Field {

    public int x;
    public int y;

    public Field(int x, int y) {
      this.x = x;
      this.y = y;
    }


    public Field() {
    }
  }
}
