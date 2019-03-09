import {ChessGameMetadata} from '../../model/ChessGameMetadata';
import {SingleHeaderFilter} from './headers-filter/headers-filter.component';
import {BasicHeaderKeys} from '../../model/BasicHeaderKeys';

export class FilterConversionUtil {

  static chessGameMetadataToSingleHeaderFilterArray(chessGameMetadata: ChessGameMetadata): SingleHeaderFilter[] {
    let result = [];
    if (chessGameMetadata == null) {
      return result;
    }
    for (let basicKey of BasicHeaderKeys.keys) {
      if (chessGameMetadata[basicKey] != null) {
        result.push({key: basicKey, value: chessGameMetadata[basicKey]});
      }
    }
    if (chessGameMetadata.additional != null) {
      for (let additionalKey of Object.keys(chessGameMetadata.additional)) {
        result.push({key: additionalKey, value: chessGameMetadata.additional[additionalKey]});
      }
    }
    return result;
  }

  static singleHeaderFilterArrayToChessGameMetadata(shfa: SingleHeaderFilter[]) {
    if (shfa == null) {
      return null;
    }
    let result = new ChessGameMetadata();
    result.additional = {};
    shfa.filter(f => f.key.length > 0 && f.value.length > 0 &&
      BasicHeaderKeys.keys.includes(f.key))
      .forEach(basic => result[basic.key] = basic.value);
    shfa.filter(f => f.key.length > 0 && f.value.length > 0 &&
      !BasicHeaderKeys.keys.includes(f.key))
      .forEach(additional => result.additional[additional.key] = additional.value);
    return result;
  }

}
