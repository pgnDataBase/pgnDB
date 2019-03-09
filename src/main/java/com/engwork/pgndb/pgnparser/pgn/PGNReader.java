package com.engwork.pgndb.pgnparser.pgn;


import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 07.07.2017.
 */

public class PGNReader {

  private int gameNo = 0;
  private boolean inSingleLineComment = false;
  private boolean inMultiLineComment = false;
  private StringBuilder buffer = new StringBuilder();
  private Meta meta = new Meta();
  private State state;
  private PGNGame game;
  private List<PGNGame> games;

  public PGNReader() {
    games = new ArrayList<>();
    game = new PGNGame();
    game.setId(gameNo);
  }

  public List<PGNGame> read(Reader reader) throws IOException {
    state = this::readMetaKey;
    int c;
    while ((c = reader.read()) != -1) {
      state.process((char) c);
    }
    endParsingGame();
    return games;
  }

  private void readMetaKey(char c) {
    switch (c) {
      case '\\':
        break;
      case '[':
        meta = new Meta();
        break;
      case '"':
        meta.setKey(buffer.toString());
        buffer = new StringBuilder();
        state = this::readMetaValue;
        break;
      default:
        buffer.append(c);
        break;
    }
  }

  private void readMetaValue(char c) {
    switch (c) {
      case '\\':
        break;
      case '"':
        break;
      case ']':
        meta.setValue(buffer.toString());
        game.getMeta().add(meta);
        meta = new Meta();
        buffer = new StringBuilder();
        state = this::readNextChar;
        break;
      default:
        buffer.append(c);
        break;

    }
  }

  private void readNextChar(char c) {
    if (!Character.isWhitespace(c)) {
      if (c == '[') {
        state = this::readMetaKey;
        state.process(c);
      } else {
        state = this::readMoveText;
        state.process(c);
      }
    }
  }

  private void readMoveText(char c) {
    switch (c) {
      case '{':
        inMultiLineComment = true;
        buffer.append(c);
        break;
      case ';':
        inSingleLineComment = true;
        buffer.append(c);
        break;
      case '\n':
        if (inSingleLineComment) {
          inSingleLineComment = false;
        }
        buffer.append(c);
        break;
      case '}':
        if (inMultiLineComment) {
          inMultiLineComment = false;
        }
        buffer.append(c);
        break;
      case '[':
        if (!inMultiLineComment && !inSingleLineComment) {
          endParsingGame();
          state = this::readNextChar;
          state.process(c);
        } else {
          buffer.append(c);
        }
        break;
      default:
        buffer.append(c);
        break;
    }
  }

  private void endParsingGame() {
    MoveTextReader reader = new MoveTextReader();
    game.setEntities(reader.readMoves(buffer.toString()));
    games.add(game);
    game = new PGNGame();
    gameNo++;
    game.setId(gameNo);
    buffer = new StringBuilder();
  }

  @FunctionalInterface
  private interface State {

    void process(char c);

  }

}
