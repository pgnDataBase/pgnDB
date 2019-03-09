package com.engwork.pgndb.chessgamesconverter;

import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.exceptionhandling.OperationException;
import com.engwork.pgndb.pgnparser.ParsedChessGame;
import com.engwork.pgndb.pgnparser.ParsedChessGameImpl;
import com.engwork.pgndb.pgnparser.PossibleMovesProviderImpl;
import com.engwork.pgndb.pgnparser.SANMoveMaker;
import com.engwork.pgndb.pgnparser.exceptions.WrongMoveException;
import com.engwork.pgndb.pgnparser.pgn.PGNGame;
import com.engwork.pgndb.pgnparser.pgn.PGNReader;
import com.engwork.pgndb.pgnparser.pgn.PGNWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChessGamesConverter {

  private ChessGameDataBuilder chessGameDataBuilder;
  private PGNGameBuilder pgnGameBuilder;

  //Parse pgn string into list of PGNGames
  private List<PGNGame> readGamesFromFile(String pgnString) {
    List<PGNGame> games;
    Reader reader;
    try {
      //Convert string to reader
      reader = new StringReader(pgnString);
      PGNReader pgnReader = new PGNReader();
      //Read games
      games = pgnReader.read(reader);
      reader.close();
    } catch (IOException e) {
      games = null;
    }
    if (games != null) {
      int gameNo = 0;
      //Iterate over all extracted games, to process entities and extract info about valid moves
      for (PGNGame pgnGame : games) {
        ParsedChessGame parsedChessGame = new ParsedChessGameImpl();
        PossibleMovesProviderImpl possibleMovesProvider = new PossibleMovesProviderImpl(parsedChessGame);
        SANMoveMaker sanMoveMaker = new SANMoveMaker(parsedChessGame, possibleMovesProvider);
        //Process entities, extract info about each move in this game
        try {
          sanMoveMaker.processMoves(pgnGame.getEntities());
        } catch (WrongMoveException e) {
          ChessGameMetadata chessGameMetadata = ChessGameMetadata.create(pgnGame.getMeta());
          String tags = String.format("Event: %s | Site: %s | White: %s | Black: %s",
                                      chessGameMetadata.getEvent(), chessGameMetadata.getSite(), chessGameMetadata.getWhitePlayer(), chessGameMetadata.getBlackPlayer());
          String reason = String.format("Invalid move '%s' in game %d with tags { %s }", e.getMoveSan(), gameNo + 1, tags);
          throw new OperationException("PGN parsing operation", reason);
        }
        gameNo++;
      }
    }
    return games;
  }

  // Read games from pgn string and parse it into our model
  public List<ChessGameData> convertPGNIntoChessGamesData(String pgnString) {
    //Read games from parser
    List<PGNGame> pgnGames = readGamesFromFile(pgnString);
    List<ChessGameData> chessGameDataList = new ArrayList<>();
    //Convert each PGNGame to format supported in our app
    for (PGNGame pgnGame : pgnGames) {
      ChessGameData chessGameData = chessGameDataBuilder.create().build(pgnGame);
      chessGameDataList.add(chessGameData);
    }
    return chessGameDataList;
  }


  private PGNGame fromChessGameDataToPGNGame(ChessGameData chessGameData) {
    return pgnGameBuilder.create().build(chessGameData);
  }

  private String fromPGNGameToString(PGNGame pgnGame) {
    PGNWriter pgnWriter = new PGNWriter();
    return pgnWriter.write(pgnGame.getEntities(), pgnGame.getMeta());
  }

  public String convertChessGameDataIntoPGN(ChessGameData chessGameData) {
    PGNGame pgnGame = fromChessGameDataToPGNGame(chessGameData);
    return fromPGNGameToString(pgnGame);
  }
}
