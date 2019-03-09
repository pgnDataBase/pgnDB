import {Move} from '../../model/Move';

export class VariantSwapUtil {
  public static swapNeighbourVariants(moveList: Move[], variantId1: number, variantId2: number) {
    if (!VariantSwapUtil.areVariantsNeighbour(moveList, variantId1, variantId2)) {
      throw 'Variants ' + variantId1 + ' ' + variantId2 + ' are not neighbours!';
    }
    let firstStart = moveList.findIndex(mv => mv.variantId === variantId1 && mv.variantType === 'VB');
    let secondStart = moveList.findIndex(mv => mv.variantId === variantId2 && mv.variantType === 'VB');
    let firstVariantIsEarlier = firstStart < secondStart;
    let swapStartIndex = firstVariantIsEarlier ? firstStart : secondStart;
    let firstOnlyPlusNested = VariantSwapUtil.movesForVariantIdWithNested(moveList, variantId1);
    let secondOnlyPlusNested = VariantSwapUtil.movesForVariantIdWithNested(moveList, variantId2);
    let bothVariants = firstVariantIsEarlier ?
      secondOnlyPlusNested.concat(firstOnlyPlusNested) : firstOnlyPlusNested.concat(secondOnlyPlusNested);
    let curr = 0;
    for (let i = swapStartIndex; curr < bothVariants.length; i++) {
      moveList[i] = bothVariants[curr];
      curr++;
    }
  }

  public static movesForVariantIdWithNested(moveList: Move[], variantId: number): Move[] {
    let result = [];
    let firstInVariant = moveList.findIndex(mv => mv.variantId === variantId && mv.variantType === 'VB');
    for (let counter = firstInVariant; counter < moveList.length; counter++) {
      result.push(moveList[counter]);
      if (moveList[counter].variantId === variantId && moveList[counter].variantType === 'VE') {
        break;
      }
    }
    return result;
  }

  public static areVariantsNeighbour(moveList: Move[], variantId1: number, variantId2: number) {
    let firstStart = -1;
    let firstEnd = -1;
    let secondStart = -1;
    let secondEnd = -1;
    for (let i = 0; i < moveList.length; i++) {
      let move = moveList[i];
      if (move.variantId === variantId1 && move.variantType === 'VB') {
        firstStart = i;
      }
      if (move.variantId === variantId1 && move.variantType === 'VE') {
        firstEnd = i;
      }
      if (move.variantId === variantId2 && move.variantType === 'VB') {
        secondStart = i;
      }
      if (move.variantId === variantId2 && move.variantType === 'VE') {
        secondEnd = i;
      }
    }
    [firstStart, firstEnd, secondStart, secondEnd].forEach(v => {
      if (v === -1) {
        throw "Variant beginning or end not found!";
      }
    });
    return Math.abs(secondEnd - firstStart) === 1 || Math.abs(firstEnd - secondStart) === 1;
  }

  public static canSwapToLeft(moveList: Move[], currentMove: Move) {
    let vId = currentMove.variantId;
    if (vId == null) {
      return false;
    }
    let foundOwnStart: boolean = false;
    let idx = moveList.findIndex(mv => mv === currentMove);
    for (idx; idx >= 0; idx--) {
      if (moveList[idx].variantId === vId && moveList[idx].variantType === 'VB') {
        foundOwnStart = true;
      }
      if (foundOwnStart && moveList[idx].variantId !== vId) {
        return moveList[idx].variantId != null && moveList[idx].variantType === 'VE';
      }
    }
    return false;
  }

  public static canSwapToRight(moveList: Move[], currentMove: Move) {
    let vId = currentMove.variantId;
    if (vId == null) {
      return false;
    }
    let foundOwnEnd: boolean = false;
    let idx = moveList.findIndex(mv => mv === currentMove);
    for (idx; idx < moveList.length; idx++) {
      if (moveList[idx].variantId === vId && moveList[idx].variantType === 'VE') {
        foundOwnEnd = true;
      }
      if (moveList[idx].variantId !== vId && foundOwnEnd) {
        return moveList[idx].variantId != null && moveList[idx].variantType === 'VB';
      }
    }
  }

  public static getLeftNeighbourVariantId(moveList: Move[], currentMove: Move) {
    let idx = moveList.findIndex(mv => mv === currentMove);
    let foundCurrVarBeginning: boolean = false;
    for (idx; idx >= 0; idx--) {
      if (moveList[idx].variantId === currentMove.variantId && moveList[idx].variantType === 'VB') {
        foundCurrVarBeginning = true;
      }
      if (moveList[idx].variantId !== currentMove.variantId && foundCurrVarBeginning) {
        return moveList[idx].variantId;
      }
    }
    throw 'No left neighbour found';
  }

  public static getRightNeighbourVariantId(moveList: Move[], currentMove: Move) {
    let idx = moveList.findIndex(mv => mv === currentMove);
    let foundCurrVarEnd: boolean = false;
    for (idx; idx < moveList.length; idx++) {
      if (moveList[idx].variantId === currentMove.variantId && moveList[idx].variantType === 'VE') {
        foundCurrVarEnd = true;
      }
      if (moveList[idx].variantId !== currentMove.variantId && foundCurrVarEnd === true) {
        return moveList[idx].variantId;
      }
    }
    throw 'No right neighbour found';
  }
}
