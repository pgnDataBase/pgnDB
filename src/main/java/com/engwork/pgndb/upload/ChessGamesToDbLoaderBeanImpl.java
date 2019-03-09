package com.engwork.pgndb.upload;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesloader.ChessGamesLoaderBean;
import com.engwork.pgndb.statusresolver.StatusResolver;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class ChessGamesToDbLoaderBeanImpl implements ChessGamesToDbLoaderBean {

  private FileToChessGamesConverterBean fileToChessGamesConverterBean;
  private ChessGamesLoaderBean chessGamesLoaderBean;
  private StatusResolver loadingStatusResolver;

  private static final Integer CHUNK_SIZE = 2000;
  private static final String GAMES_DELIMITER_REGEX = "\\[Event\\u0020";
  private static final String GAMES_DELIMITER_STRING = "[Event ";
  private static final String NEW_LINES = "\r\n\r\n";

  @Override
  public void loadGamesToDatabase(File file, String databaseName) throws FileNotFoundException {
    this.loadGamesToDatabase(fileToString(file), databaseName);
  }

  @Override
  public void loadGamesToDatabase(String pgn, String databaseName) {
    List<String> fileChunks = chunkPGN(pgn);
    loadingStatusResolver.initStatus(databaseName, 1.0 / fileChunks.size());
    for (int index = 0; index < fileChunks.size(); index++) {
      List<ChessGameData> chessGames = fileToChessGamesConverterBean.getChessGamesFromString(fileChunks.get(index));
      log.info("Started chunk conversion [{}/{}]", index + 1, fileChunks.size());
      chessGamesLoaderBean.loadChessGames(chessGames, databaseName, true);
      loadingStatusResolver.updateStatusByKey(databaseName, ((1.0 * (index + 1)) / (fileChunks.size())) * 100);
      log.info("Loading games to database [{}]. Progress {}%.", databaseName, loadingStatusResolver.getStatusByKey(databaseName).getValue());
    }
    loadingStatusResolver.removeByKey(databaseName);
  }

  private String fileToString(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    String text = scanner.useDelimiter("\\A").next();
    scanner.close();
    return text;
  }

  private List<String> chunkPGN(String pgnString) {
    String[] elements = pgnString.trim().split(GAMES_DELIMITER_REGEX);
    int elementsNumber = elements.length;

    List<String> result = new ArrayList<>();
    List<String> chunk = new ArrayList<>();
    for (int index = 0; index < elementsNumber; index++) {
      if (!elements[index].isEmpty()) {
        chunk.add(GAMES_DELIMITER_STRING + elements[index].trim());
      }
      if (chunk.size() == CHUNK_SIZE || index == elementsNumber - 1) {
        result.add(String.join(NEW_LINES, chunk));
        chunk.clear();
      }
    }
    return result;
  }

}
