package com.engwork.pgndb.upload;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

interface FileToChessGamesConverterBean {

  List<ChessGameData> getChessGamesFromFile(File file) throws FileNotFoundException;

  List<ChessGameData> getChessGamesFromString(String chessGamesPgnString);
}
