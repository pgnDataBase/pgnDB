import {Move} from '../../model/Move';
import {VariantDepthDisplayCalculator} from './variant-depth-display-calculator';

function createTestMoveList(): Move[] {
  let result = [];
  let m1 = new Move(); m1.variantId = null; m1.variantType = null; result.push(m1);
  let m2 = new Move(); m2.variantId = 1; m2.variantType = 'VB'; result.push(m2);
  let m3 = new Move(); m3.variantId = 1; m3.variantType = 'VI'; result.push(m3);
  let m4 = new Move(); m4.variantId = 1; m4.variantType = 'VE'; result.push(m4);
  let m5 = new Move(); m5.variantId = null; m5.variantType = null; result.push(m5);
  let m6 = new Move(); m6.variantId = 2; m6.variantType = 'VB'; result.push(m6);
  let m7 = new Move(); m7.variantId = 3; m7.variantType = 'VB'; result.push(m7);
  let m8 = new Move(); m8.variantId = 3; m8.variantType = 'VI'; result.push(m8);
  let m9 = new Move(); m9.variantId = 3; m9.variantType = 'VI'; result.push(m9);
  let m10 = new Move(); m10.variantId = 3; m10.variantType = 'VE'; result.push(m10);
  let m11 = new Move(); m11.variantId = 4; m11.variantType = 'VB'; result.push(m11);
  let m12 = new Move(); m12.variantId = 4; m12.variantType = 'VE'; result.push(m12);
  let m13 = new Move(); m13.variantId = 2; m13.variantType = 'VI'; result.push(m13); // czternastke wcielo przy regresji ;)
  let m15 = new Move(); m15.variantId = 2; m15.variantType = "VE"; result.push(m15);
  let m16 = new Move(); m16.variantId = null; m16.variantType = null; result.push(m16);
  let m17 = new Move(); m17.variantId = null; m17.variantType = null; result.push(m17);
  let m18 = new Move(); m18.variantId = 6; m18.variantType = 'VB'; result.push(m18);
  let m19 = new Move(); m19.variantId = 6; m19.variantType = 'VI'; result.push(m19);
  let m20 = new Move(); m20.variantId = 6; m20.variantType = 'VE'; result.push(m20);
  let m21 = new Move(); m21.variantId = null; m21.variantType = null; result.push(m21);
  return result;
}

describe('VariantDepthDisplayCalculator', () => {
  it('should caclulate values properly', () => {
    let calculationResult = VariantDepthDisplayCalculator.calculateForMoveList(createTestMoveList());

    expect(calculationResult[1].depthLevel).toBe(1);
    expect(calculationResult[1].hasMoreVariantsInside).toBe(undefined);

    expect(calculationResult[2].depthLevel).toBe(1);
    expect(calculationResult[2].hasMoreVariantsInside).toBe(true);

    expect(calculationResult[3].depthLevel).toBe(2);
    expect(calculationResult[3].hasMoreVariantsInside).toBe(undefined);

    expect(calculationResult[4].depthLevel).toBe(2);
    expect(calculationResult[4].hasMoreVariantsInside).toBe(undefined);

    expect(calculationResult[6].depthLevel).toBe(1);
    expect(calculationResult[6].hasMoreVariantsInside).toBe(undefined);
  });
});
