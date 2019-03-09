import {Move} from '../../model/Move';
import {VariantSwapUtil} from './variant-swap-util';

function simpleVariantMove(variantId: number, variantType: 'VB' | 'VI' | 'VE') {
  let result = new Move();
  result.variantId = variantId;
  result.variantType = variantType;
  return result;
}

function simpleVariantList(): Move[] {
  let simpleList: Move[] = [];
  simpleList.push(simpleVariantMove(null, null));
  simpleList.push(simpleVariantMove(1, 'VB'));
  simpleList.push(simpleVariantMove(1, 'VI'));
  simpleList.push(simpleVariantMove(1, 'VE'));
  simpleList.push(simpleVariantMove(2, 'VB'));
  simpleList.push(simpleVariantMove(2, 'VE'));
  simpleList.push(simpleVariantMove(3, 'VB'));
  simpleList.push(simpleVariantMove(3, 'VE'));
  simpleList.push(simpleVariantMove(null, null));
  return simpleList;
}

describe('VariantSwapUtil', () => {

  it('Should swap variants', () => {
    let simpleList = simpleVariantList();
    VariantSwapUtil.swapNeighbourVariants(simpleList, 1, 2);
    expect(simpleList[1].variantId).toBe(2);
    expect(simpleList[2].variantId).toBe(2);
    expect(simpleList[3].variantId).toBe(1);
    expect(simpleList[4].variantId).toBe(1);
    expect(simpleList[5].variantId).toBe(1);
  });

  it('Should say that variants are neighbours', () => {
    let simpleList = simpleVariantList();
    let areNeighbours = VariantSwapUtil.areVariantsNeighbour(simpleList, 1, 2);
    let areNeighboursReversed = VariantSwapUtil.areVariantsNeighbour(simpleList, 1, 2);

    expect(areNeighbours).toBe(true);
    expect(areNeighboursReversed).toBe(true);
  });

  it('Should say that variants are neighbours', () => {
    let simpleList = simpleVariantList();
    let areNeighbours = VariantSwapUtil.areVariantsNeighbour(simpleList, 1, 3);
    expect(areNeighbours).toBe(false);
  });
});
