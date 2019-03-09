import {ChessGameMetadata} from './ChessGameMetadata';

export class HeadersAndPositionFilter {
  headerFilter: ChessGameMetadata;
  positionsFilter: {
    positions: string[];
    includeVariants: boolean;
  };
}
