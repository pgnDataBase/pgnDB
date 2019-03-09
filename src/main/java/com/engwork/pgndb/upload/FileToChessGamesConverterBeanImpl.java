package com.engwork.pgndb.upload;

import com.engwork.pgndb.chessgamesconverter.ChessGamesConverter;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

@AllArgsConstructor
@Slf4j
class FileToChessGamesConverterBeanImpl implements FileToChessGamesConverterBean {

  private ChessGamesConverter chessGamesConverter;

  @Override
  public List<ChessGameData> getChessGamesFromFile(File file) throws FileNotFoundException {
    String textFromFile = getStringFromProvidedFile(file);
    return this.convertPgnStringToChessGames(textFromFile);
  }

  @Override
  public List<ChessGameData> getChessGamesFromString(String chessGamesPgnString) {
    return this.convertPgnStringToChessGames(chessGamesPgnString);
  }

  private List<ChessGameData> convertPgnStringToChessGames(String chessGamesPgnString) {
    return chessGamesConverter.convertPGNIntoChessGamesData(chessGamesPgnString);
  }

  private String getStringFromProvidedFile(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    String text = scanner.useDelimiter("\\A").next();
    scanner.close();
    return text;
  }

}
