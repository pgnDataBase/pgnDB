import {Move} from './Move';
import {ChessGameMetadata} from './ChessGameMetadata';

export class ChessGame {
  chessGameMetadata: ChessGameMetadata;
  moveList: Move[];
  // signifies if game was ever saved to database - used in new game addition
  notPersisted: boolean;

  public static emptyGame() {
    let game = new ChessGame();
    game.moveList = [];
    game.notPersisted = true;
    let metadata = new ChessGameMetadata();
    metadata.additional = {};
    game.chessGameMetadata = metadata;
    return game;
  }
}
