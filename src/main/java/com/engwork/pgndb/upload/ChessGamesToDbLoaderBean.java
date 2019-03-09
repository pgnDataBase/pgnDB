package com.engwork.pgndb.upload;

import java.io.File;
import java.io.FileNotFoundException;


public interface ChessGamesToDbLoaderBean {

  void loadGamesToDatabase(File file, String database) throws FileNotFoundException;

  void loadGamesToDatabase(String pgn, String databaseName);

}
