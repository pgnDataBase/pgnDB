import {PaginationContext} from './pagination.service';

export class PaginationContextsHandler {

  public static getPaginationContexts(): PaginationContext[] {
    let contexts = [];
    let chessGamesContext: PaginationContext = {
      contextName: 'games',
      currentPage: 0,
      pageSize: 10,
      itemsTotal: 0
    };
    let chessGameEntitiesContext: PaginationContext = {
      contextName: 'entities',
      currentPage: 0,
      pageSize: 600,
      itemsTotal: 0
    };
    contexts.push(chessGamesContext);
    contexts.push(chessGameEntitiesContext);
    return contexts;
  }

  public static previousDBNameForPagination = null;

}
