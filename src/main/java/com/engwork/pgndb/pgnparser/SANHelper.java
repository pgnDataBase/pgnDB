package com.engwork.pgndb.pgnparser;

import com.engwork.pgndb.pgnparser.consts.Piece;
import com.engwork.pgndb.pgnparser.consts.PieceColor;
import com.engwork.pgndb.pgnparser.entities.Move;

import java.util.HashMap;

/**
 * Created by Stanisław Kabaciński.
 */

public class SANHelper implements PieceColor, Piece {

  public static HashMap<String, Character> PIECE_CODE_TO_CHAR = new HashMap<>();

  static {
    PIECE_CODE_TO_CHAR.put("&#9812;", 'K');
    PIECE_CODE_TO_CHAR.put("&#9818;", 'K');
    PIECE_CODE_TO_CHAR.put("&#9813;", 'Q');
    PIECE_CODE_TO_CHAR.put("&#9819;", 'Q');
    PIECE_CODE_TO_CHAR.put("&#9814;", 'R');
    PIECE_CODE_TO_CHAR.put("&#9820;", 'R');
    PIECE_CODE_TO_CHAR.put("&#9815;", 'B');
    PIECE_CODE_TO_CHAR.put("&#9821;", 'B');
    PIECE_CODE_TO_CHAR.put("&#9816;", 'N');
    PIECE_CODE_TO_CHAR.put("&#9822;", 'N');
  }

  public static String getSymbol(int pieceWithColor) {
    switch (pieceWithColor) {
      case KING + WHITE:
        return "&#9812;";
      case QUEEN + WHITE:
        return "&#9813;";
      case KS_ROOK + WHITE:
      case QS_ROOK + WHITE:
        return "&#9814;";
      case BISHOP + WHITE:
        return "&#9815;";
      case KNIGHT + WHITE:
        return "&#9816;";
      case PAWN + WHITE:
        return "";
      case KING + BLACK:
        return "&#9818;";
      case QUEEN + BLACK:
        return "&#9819;";
      case KS_ROOK + BLACK:
      case QS_ROOK + BLACK:
        return "&#9820;";
      case BISHOP + BLACK:
        return "&#9821;";
      case KNIGHT + BLACK:
        return "&#9822;";
      case PAWN + BLACK:
        return "";
      default:
        return "";
    }
  }

  public static String replaceSymbolsWithLetters(String san) {
    String out = san;
    int begin = san.indexOf('&');
    if (begin >= 0) {
      int end = san.indexOf(';');
      String symbol = san.substring(begin, end + 1);
      out = san.replace(symbol, Character.toString(PIECE_CODE_TO_CHAR.get(symbol)));
    }
    out = out.replace(':', 'x');
    return out;
  }

  public static String getField(int x, int y) {
    char fieldLetter = getFile(x);
    return String.valueOf(fieldLetter) + (y + 1);
  }

  public static char getFile(int x) {
    return (char) (x + 'a');
  }

  public static int getPieceFromChar(char c) {
    c = Character.toUpperCase(c);
    switch (c) {
      case 'K':
        return KING;
      case 'Q':
        return QUEEN;
      case 'R':
        return KS_ROOK;
      case 'B':
        return BISHOP;
      case 'N':
        return KNIGHT;
      default:
        return PAWN;
    }
  }

  public static String getSanFromLAN(String lan, ParsedChessGame parsedChessGame) {
    if (lan == null || lan.isEmpty()) return null;
    int movesCount = parsedChessGame.getMoveList().size();
    MoveFactory moveFactory = new MoveFactory(new PossibleMovesProviderImpl(parsedChessGame));
    String[] moves = lan.split(" ");
    for (String s : moves) {
      char[] chars = s.trim().toCharArray();
      if (chars.length >= 4) {
        Move move = moveFactory.getMove(parsedChessGame.getBoard(), chars[0] - 'a', chars[1] - '1', chars[2] - 'a', chars[3] - '1');
        if (chars.length == 5) {
          int piece = SANHelper.getPieceFromChar(chars[4]);
          move.setPromotion(piece);
          move.setSan(move.getSan() + "=" + SANHelper.getSymbol(move.getPromotion() + move.getColor()));
          parsedChessGame.getBoard()[move.getToX()][move.getToY()] = move.getPromotion() + move.getColor();
        }
        parsedChessGame.makeMove(move);
      }
    }

    String out = "";
    if (movesCount % 2 == 1) {
      out += movesCount / 2 + 1 + "… ";
    }
    for (int i = movesCount; i < parsedChessGame.getMoveList().size(); i++) {
      Move m = parsedChessGame.getMoveList().get(i);
      out += (i % 2 == 0 ? i / 2 + 1 + ". " : "") + m.getSan() + " ";
    }
    return out.trim();
  }

  public static String replaceLettersWithSymbols(String san, int color) {
    int piece = getPieceFromChar(san.charAt(0));
    if (piece != Piece.PAWN) {
      return getSymbol(piece + color) + san.substring(1);
    }
    return san;
  }
}
