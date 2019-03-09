import {Move} from '../../model/Move';

/*
  variantId -> {
              hasMoreVariantsInside: boolean (determines line break) if not its undefined
              depthLevel: number (how much nested variant is)
           }
 */

// @REFACTOR - after model change it became less readable when there's time make it more readable

export class VariantDepthDisplayCalculator {

  public static calculateForMoveList(moveList: Move[]) {
    if (moveList == null || moveList.length == 0) {
      return {};
    }
    let result = {};
    let previouslySeenMove: Move = null;
    let currentVariantDepth = 0;
    let searchState: 'VE' | 'VI' | 'VB' = null;
    for (let move of moveList) {
      if (move.variantId != null) {
        if (previouslySeenMove != null && previouslySeenMove.variantId == null) {
          // PREV (nie bylo wariantu) && TERAZ (jest wariant)
          result[move.variantId] = {};
          currentVariantDepth = 1;
          result[move.variantId].depthLevel = currentVariantDepth;
        }
        else if (previouslySeenMove != null) {
          // PREV (byl wariant)
          if (move.variantType == 'VB') {
            currentVariantDepth++;
            if (result[move.variantId] == null) {
              result[move.variantId] = {};
              result[move.variantId].depthLevel = currentVariantDepth;
            }
            if (previouslySeenMove.variantId !== move.variantId && searchState !== 'VE') {
              if (result[previouslySeenMove.variantId] != null) {
                result[previouslySeenMove.variantId].hasMoreVariantsInside = true;
              }
            }
          }
          if (move.variantType == 'VE') {
            currentVariantDepth--;
          }
        }
      }
      searchState = move.variantType;
      previouslySeenMove = move;
    }
    return result;
  }
}
