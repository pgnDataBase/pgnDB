export class PieceToUnicodeTranslationUtil {

  public static pieceToUnicode(pieceCode) {
    const color = pieceCode.substr(0, 1);
    const piece = pieceCode.substr(1, 2);
    if (piece === 'PN') {
      // pawn
      return color === 'W' ? '&#9817;' : '&#9823;';
    }
    if (piece === 'KT') {
      // knight
      return color === 'W' ? '&#9816;' : '&#9822;';
    }
    if (piece === 'BP') {
      // bishop
      return color === 'W' ? '&#9815;' : '&#9821;';
    }
    if (piece === 'QN') {
      // queen
      return color === 'W' ? '&#9813;' : '&#9819;';
    }
    if (piece === 'RK') {
      // rook
      return color === 'W' ? '&#9814;' : '&#9820;';
    }
    if (piece === 'KG') {
      // king
      return color === 'W' ? '&#9812;' : '&#9818;';
    }
  }
}
