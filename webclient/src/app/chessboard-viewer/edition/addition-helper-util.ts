import {ChessGame} from '../../model/ChessGame';
import {Move} from '../../model/Move';

export class AdditionHelperUtil {

  public static properlyInsertMove(currentGame: ChessGame, currentMove: Move, newMoveFromServer: Move): Move {
    let whereIsCurrent = currentGame.moveList.findIndex(m => m === currentMove);

    // if inserting at the end of game simply elongate the main game path
    if (whereIsCurrent === currentGame.moveList.length - 1) {
      currentGame.moveList.splice(whereIsCurrent + 1, 0, newMoveFromServer);
    } else {
      let withSameVariant = currentGame.moveList.filter(mv => mv.variantId === currentMove.variantId);

      // if currentMove is last in variant (not counting VE marker) elongate the variant
      if (currentMove.variantId != null && currentMove === withSameVariant[withSameVariant.length - 2]) {
        newMoveFromServer.variantId = currentMove.variantId;
        currentGame.moveList.splice(whereIsCurrent + 1, 0, newMoveFromServer);
      } // else insert new variant
      else {
        let biggestVariantId = currentGame.moveList.reduce((a,b) => a.variantId > b.variantId ? a : b).variantId;
        newMoveFromServer.variantId = biggestVariantId + 1;
        newMoveFromServer.variantType = 'VB';
        let variantEndMarker = new Move();
        variantEndMarker.variantId = biggestVariantId + 1;
        variantEndMarker.variantType = 'VE';
        currentGame.moveList.splice(whereIsCurrent + 2, 0, newMoveFromServer, variantEndMarker);
      }
    }
    return newMoveFromServer;
  }
}

