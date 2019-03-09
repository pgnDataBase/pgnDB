package com.engwork.pgndb.pgnparser.pgn;


//import android.support.annotation.NonNull;

import com.engwork.pgndb.pgnparser.entities.Comment;
import com.engwork.pgndb.pgnparser.entities.Entity;
import com.engwork.pgndb.pgnparser.entities.GameBegin;
import com.engwork.pgndb.pgnparser.entities.Move;
import com.engwork.pgndb.pgnparser.entities.VariantBegin;
import com.engwork.pgndb.pgnparser.entities.VariantEnd;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanisław Kabaciński.
 */

@NoArgsConstructor
public class MoveTextReader {

  private static final String TAG = MoveTextReader.class.getSimpleName();

  interface Token {
    int EOS = 0;
    int MOVE_NUMBER = 1;
    int MOVE = 2;
    int NAG = 3;
    int RAV_BEGIN = 4;
    int RAV_END = 5;
    int COMMENT_BEGIN = 6;
    int COMMENT_END = 7;
    int RESULT = 8;
    int LINE_COMMENT_BEGIN = 9;
  }

  private int lastPos = 0;

  public PGNGame read(String string) {
    lastPos = 0;
    List<Meta> meta = new ArrayList<>();
    List<Entity> entities = new ArrayList<>();
    PGNGame game = new PGNGame(meta, entities);
    meta.addAll(readMeta(string));
    game.setEntities(readMoves(string));

    return game;
  }

  private List<Meta> readMeta(String string) {
    List<Meta> meta = new ArrayList<>();

    int begin = 0;
    int end = 0;
    while ((begin = string.indexOf('[', lastPos)) >= 0 && (end = string.indexOf("\"]", begin)) >= 0) {
      String line = string.substring(begin + 1, end);
      int splitPos = line.indexOf(" \"");
      if (splitPos >= 0) {
        meta.add(new Meta(line.substring(0, splitPos), line.substring(splitPos + 2), false));
      }
      lastPos = end;
    }

    return meta;
  }

  public List<Entity> readMoves(String string) {
    List<Entity> entities = new ArrayList<>();
    entities.add(new GameBegin());
    int token;
    while ((token = next(string)) != Token.EOS) {
      switch (token) {
        case Token.MOVE_NUMBER: {
          lastPos = string.indexOf('.', lastPos) + 1;
          break;
        }
        case Token.RESULT:
        case Token.MOVE: {
          Move move = new Move();
          move.setSan(getSymbol(string).replace('x', ':'));
          if(!isScore(move.getSan()))
            entities.add(move);
          break;
        }
        case Token.COMMENT_BEGIN: {
          Comment comment = new Comment(getComment(string));
          entities.add(comment);
          break;
        }
        case Token.RAV_BEGIN: {
          entities.add(new VariantBegin());
          lastPos++;
          break;
        }
        case Token.RAV_END: {
          entities.add(new VariantEnd());
          lastPos++;
          break;
        }
        case Token.NAG: {
          String nag = getNag(string).trim().substring(1);
          try {
            int n = Integer.parseInt(nag);
            if (entities.get(entities.size() - 1) instanceof Move) {
              Move m = (Move) entities.get(entities.size() - 1);
              int[] nags = m.getNag();
              int[] newNags = new int[nag.length() + 1];
              for (int i = 0; i < nags.length; i++) {
                newNags[i] = nags[i];
              }
              newNags[newNags.length - 1] = n;
              m.setNag(newNags);
            }
          } catch (NumberFormatException e) {
            System.err.println("Wrong NAG format: " + nag);
            e.printStackTrace();
          }
          break;
        }
      }
    }

    return entities;
  }
  private boolean isScore(String san){
    return (san.equals("*")
        || san.equals("1-0")
        || san.equals("0-1")
        || san.equals("1/2-1/2")
        || san.equals("Z0"));
  }
  private int next(String string) {
    while (lastPos < string.length()) {
      char c = string.charAt(lastPos);
      if (c >= '0' && c <= '9') {
        if (string.indexOf('.', lastPos) != -1) {
          return Token.MOVE_NUMBER;
        } else {
          return Token.RESULT;
        }
      }
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
        return Token.MOVE;
      }
      if (c == '$') {
        return Token.NAG;
      }
      if (c == '{') {
        return Token.COMMENT_BEGIN;
      }
      if (c == ')') {
        return Token.RAV_END;
      }
      if (c == '(') {
        return Token.RAV_BEGIN;
      }
      if (c == ';') {
        return Token.LINE_COMMENT_BEGIN;
      }
      lastPos++;
    }
    return Token.EOS;
  }

  private String getNag(String string) {
    int start = lastPos;
    int end = lastPos + 1;
    char c = string.charAt(end);
    while (c >= '0' && c <= '9') {
      end++;
      c = string.charAt(end);
    }
    lastPos = end + 1;
    return string.substring(start, end + 1);
  }

  private String getSymbol(String string) {
    int start = lastPos;
    int end = lastPos + 1;
    while (end < string.length() && isSymbolChar(string.charAt(end))) {
      end++;
    }
    lastPos = end++;
    if (end > string.length()) {
      return string.substring(start);
    }
    return string.substring(start, end - 1);
  }

  //@NonNull
  private String getComment(String string) {
    int start = lastPos;
    int end = lastPos + 1;
    while (string.charAt(end) != '}') {
      end++;
    }
    lastPos = end;
    return string.substring(start + 1, end).trim();
  }

  private boolean isSymbolChar(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
        || (c >= '0' && c <= '9')
        || c == '_' || c == '+' || c == '#' || c == '=' || c == ':' || c == '-'
        || c == '/';
  }
}
