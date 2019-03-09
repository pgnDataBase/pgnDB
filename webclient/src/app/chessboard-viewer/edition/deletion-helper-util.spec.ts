import {Move} from '../../model/Move';
import {DeletionHelperUtil} from './deletion-helper-util';
import {ChessGame} from '../../model/ChessGame';

function simpleDelMove(moveNumber: number, variantId: number, variantType: 'VB' | 'VI' | 'VE', moveId?: string) {
  let result = new Move();
  result.moveNumber = moveNumber;
  result.variantId = variantId;
  result.variantType = variantType;
  result.id = moveId;
  return result;
}

function simpleChessGame(moveList: Move[]) {
  let result = new ChessGame();
  result.moveList = moveList;
  return result;
}

describe('DeletionHelperUtil', () => {

  it('Should get variants ids dependent on move', () => {
    let simpleList: Move[] = [];
    simpleList.push(simpleDelMove(1, null, null, 'ergerger'));
    simpleList.push(simpleDelMove(1, 1, 'VB', 'rthergrge'));
    simpleList.push(simpleDelMove(2, 1, 'VI', 'ertergerg'));
    simpleList.push(simpleDelMove(2, 2, 'VB', 'htrgreger'));
    simpleList.push(simpleDelMove(3, 2, 'VE', 'erthertgerg'));
    simpleList.push(simpleDelMove(3, 1, 'VE', 'ergerher'));
    let ids = DeletionHelperUtil.getDependentVariantsIdsForMove(simpleChessGame(simpleList), simpleList[2]);
    let idsArray = Array.from(ids);
    expect(idsArray[0]).toBe(2);
  });

  it('Should delete variants dependent on move', () => {
    let simpleList: Move[] = [];
    let chessGame = simpleChessGame(simpleList);
    simpleList.push(simpleDelMove(1, null, null, 'ergerjyger'));
    simpleList.push(simpleDelMove(1, 1, 'VB', 'rthergjyrge'));
    simpleList.push(simpleDelMove(2, 1, 'VI', 'ertergerg'));
    simpleList.push(simpleDelMove(2, 2, 'VB', 'htrgreger'));
    simpleList.push(simpleDelMove(3, 2, 'VE', 'erthertgerg'));
    simpleList.push(simpleDelMove(3, 1, 'VE', 'ergerherrth'));
    simpleList.push(simpleDelMove(1, null, null, 'erhtgerher'));
    simpleList.push(simpleDelMove(1, null, null, 'ergetyjrher'));
    DeletionHelperUtil.deleteDependentVariantsForMove(chessGame, chessGame.moveList[2]);
    expect(chessGame.moveList.length).toBe(6);
  });

  it('Should not return dependent on variant end', () => {
    let simpleList: Move[] = [];
    let chessGame = simpleChessGame(simpleList);
    simpleList.push(simpleDelMove(1, null, null, 'ergerjyger'));
    simpleList.push(simpleDelMove(1, 1, 'VB', 'rthergjyrge'));
    simpleList.push(simpleDelMove(2, 1, 'VI', 'ertergerg'));
    simpleList.push(simpleDelMove(2, 2, 'VB', 'htrgreger'));
    simpleList.push(simpleDelMove(3, 2, 'VE', 'erthertgerg'));
    simpleList.push(simpleDelMove(3, 1, 'VE', 'ergerherrth'));
    simpleList.push(simpleDelMove(1, null, null, 'erhtgerher'));
    simpleList.push(simpleDelMove(1, null, null, 'ergetyjrher'));
    let ids = DeletionHelperUtil.getDependentVariantsIdsForMove(chessGame, chessGame.moveList[3]);
    let idsArray = Array.from(ids);
    expect(idsArray[0]).toBe(undefined);
  });

});
