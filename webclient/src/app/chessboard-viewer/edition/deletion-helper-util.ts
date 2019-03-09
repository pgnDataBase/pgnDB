import {ChessGame} from '../../model/ChessGame';
import {Move} from '../../model/Move';

// @REFACTOR - more unit tests and rewrite to be more readable

export class DeletionHelperUtil {

  // this is called after deletion happened already
  public static updateVariantsOnMoveDeletion(currentGame: ChessGame,
                               deletedMove: Move): Move {
    let withSameVariant = currentGame.moveList.filter(mv => mv.variantId === deletedMove.variantId);
    if (withSameVariant.length === 0) {
      return null;
    }
    // VE MARKER ONLY
    if (withSameVariant.length === 1) {
      currentGame.moveList = currentGame.moveList.filter(mv => mv.variantId != deletedMove.variantId);
    }
    if (deletedMove.variantId != null) {
      return withSameVariant[withSameVariant.length - 2];
    } else {
      return withSameVariant[withSameVariant.length - 1];
    }
  }

  public static deleteVariant(currentGame: ChessGame, variantId: number) {
    DeletionHelperUtil.deleteDependentVariants(currentGame, variantId);
    currentGame.moveList = currentGame.moveList.filter(mv => mv.variantId != variantId);
  }


  public static deleteDependentVariants(currentGame: ChessGame, variantId: number) {
    let dependentVariantIds: Set<number> = DeletionHelperUtil.getDependentVariantsIds(currentGame, variantId);
    currentGame.moveList = currentGame.moveList.filter(mv => !dependentVariantIds.has(mv.variantId));
  }

  public static deleteDependentVariantsForMove(currentGame: ChessGame, move: Move) {
    let toDeleteIds: Set<number> = DeletionHelperUtil.getDependentVariantsIdsForMove(currentGame, move);
    currentGame.moveList = currentGame.moveList.filter(mv => !toDeleteIds.has(mv.variantId));
  }

  public static getDependentVariantsIdsForMove(currentGame: ChessGame, move: Move): Set<number> {
    let ids = new Set();
    let index = currentGame.moveList.findIndex(mv => mv === move);
    if (index == 0) {
      return ids;
    }
    for (index; index < currentGame.moveList.length; index++) {
      let vId = currentGame.moveList[index].variantId;
      if (vId === move.variantId && currentGame.moveList[index].variantType === 'VE') {
        break;
      }
      if (vId !== move.variantId && !ids.has(vId) && vId != null) {
        ids.add(vId);
      }
    }
    return ids;
  }

  public static getDependentVariantsIds(currentGame: ChessGame, variantId: number): Set<number> {
    let indexOfFirst = 0;
    for (indexOfFirst; indexOfFirst < currentGame.moveList.length; indexOfFirst++) {
      if (currentGame.moveList[indexOfFirst].variantId === variantId) {
        break;
      }
    }
    let ids = new Set();
    for (let currInd = indexOfFirst; currInd < currentGame.moveList.length; currInd++) {
      let mv = currentGame.moveList[currInd];
      if (mv.variantId === variantId && mv.variantType === 'VE') {
        // end of search for more nested
        break;
      }
      if (mv.variantId !== variantId && !ids.has(mv.variantId) && mv.variantId != null) {
        ids.add(mv.variantId);
      }
    }
    return ids;
  }
}
