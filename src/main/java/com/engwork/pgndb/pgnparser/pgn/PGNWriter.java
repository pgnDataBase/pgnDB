package com.engwork.pgndb.pgnparser.pgn;


import com.engwork.pgndb.pgnparser.SANHelper;
import com.engwork.pgndb.pgnparser.entities.Comment;
import com.engwork.pgndb.pgnparser.entities.Entity;
import com.engwork.pgndb.pgnparser.entities.Move;
import com.engwork.pgndb.pgnparser.entities.VariantBegin;
import com.engwork.pgndb.pgnparser.entities.VariantEnd;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Stanisław Kabaciński.
 */

@NoArgsConstructor
public class PGNWriter {

  public String write(List<Entity> entities, List<Meta> meta) {
    String out = "";
    for (Meta m : meta) {
      out += '[' + m.getKey() + " \"" + m.getValue() + "\"]\n";
    }
    out += '\n';
    out += write(entities);
    return wrapLines(out, 80);
  }

  private String write(List<Entity> entities) {
    String out = "";
    int moveNr;

    Entity prev = null;
    for (Entity e : entities) {
      if (e instanceof Move) {
        Move m = (Move) e;
        moveNr = m.getNo();
        if (moveNr % 2 == 1) {
          out += (moveNr / 2 + 1) + ". ";
        } else if (prev != null && !(prev instanceof Move)) {
          out += (moveNr / 2) + "... ";
        }
        out += SANHelper.replaceSymbolsWithLetters(m.getSan()) + " ";
        for (int n : m.getNag()) {
          out += "$" + n + " ";
        }
      } else if (e instanceof Comment) {
        out += "{" + ((Comment) e).getValue() + "} ";
      } else if (e instanceof VariantBegin) {
        out += " (";
      } else if (e instanceof VariantEnd) {
        out += ") ";
      }
      prev = e;
    }
    return out;
  }

  private String wrapLines(String s, int maxLength) {
    String out = "";
    String line = "";
    String part = "";
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '\n') {
        out += line + part + c;
        line = "";
        part = "";
        continue;
      }
      if (line.length() + part.length() >= maxLength) {
        out += line.trim() + '\n';
        line = "";
      }
      part += c;
      if (c == ' ') {
        line += part;
        part = "";
      }
    }
    out += line + part;
    return out;
  }
}
