export class Move {
  id?: string;
  fromField?: string;
  toField?: string;
  gameId?: number;
  moveType?: string;
  pieceCode?: string;
  capturedPieceCode?: string;
  promotedPieceCode?: string;
  moveNumber?: number;
  variantId?: number;
  comment?: string;
  fen?: string;
  variantType?: 'VB' | 'VI' | 'VE'; // VE is marking end of border
  entityNumber?: number;
  san?: string;
  promotionAllowed?: boolean;
}
