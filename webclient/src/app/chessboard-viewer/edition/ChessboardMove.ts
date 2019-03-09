import {Move} from '../../model/Move';

export class ChessboardMove {
  source: any;
  target: any;
  piece: any;
  newFen: any;
  oldFen: any;
  orientation: any;

  constructor(source?, target?, piece?, newFen?, oldFen?, orientation?) {
    this.source = source;
    this.target = target;
    this.piece = piece;
    this.newFen = newFen;
    this.oldFen = oldFen;
    this.orientation = orientation;
  }

  public static chessboardMoveToChessdbMove(chessboardMove: ChessboardMove): Move {
    let result = new Move();
    result.fen = chessboardMove.newFen;
    result.fromField = chessboardMove.source;
    result.toField = chessboardMove.target;
    result.pieceCode = ChessboardMove.chessboardMovePieceCodeToChessdbPieceCode(chessboardMove.piece);
    return result;
  }

  public static chessboardMovePieceCodeToChessdbPieceCode(pieceCode: string): string {
    const pieceMap = {'P' : 'PN', 'N' : 'KT', 'R': 'RK', 'B': 'BP', 'Q': 'QN', 'K': 'KG'};
    const color = pieceCode.substr(0, 1) === 'w' ? 'W' : 'B';
    return color + pieceMap[pieceCode.substr(1,1)];
  }
}


