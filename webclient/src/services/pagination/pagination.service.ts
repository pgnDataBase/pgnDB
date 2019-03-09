import {EventEmitter, Injectable, OnDestroy} from '@angular/core';
import {ModelStateService} from '../state-holders/model-state.service';
import {Subscription} from 'rxjs';
import {PaginationContextsHandler} from './pagination-contexts-handler';

export class Bounds {
  lowerBound: number;
  upperBound: number;
}


/**
 * This service is supposed to handle logical pagination of things
 * eg. pagination of chess games
 *
 * Pages are counted from 0 upwards!!!
 */
export class PaginationContext {
  contextName: string;
  currentPage: number = 0;
  pageSize: number = 10;
  itemsTotal?: number = null;
}

@Injectable({
  providedIn: 'root'
})
export class PaginationService implements OnDestroy {

  constructor(private model: ModelStateService) {
    this.subscribeToDbSubject();
  }

  // this method is somewhat too contextual for this service
  // and if this service will grow in use this should be moved elsewhere
  private subscribeToDbSubject() {
    let context = this.getContext('games');
    let sub = this.model.currentDBSubject.subscribe((db) => {
      if(db == null) {
        return;
      }
      // if db "changed" reset currentPage
      if (db.name != PaginationContextsHandler.previousDBNameForPagination ||
          db.gamesTotal != context.itemsTotal) {
        PaginationContextsHandler.previousDBNameForPagination = db.name;
        context.currentPage = 0;
      }
      context.itemsTotal = db.gamesTotal;
    });
    this.subscription.add(sub);
  }

  contexts: PaginationContext[] = PaginationContextsHandler.getPaginationContexts();
  private subscription: Subscription = new Subscription();

  paginationChanged: EventEmitter<PaginationContext> =
    new EventEmitter();

  getContext(contextName: string) {
    return this.contexts.filter((context) => {
      return context.contextName == contextName;
    })[0];
  }

  getOffsetByContextName(contextName: string) {
    let context = this.getContext(contextName);
    return context.currentPage * context.pageSize;
  }

  getPageSizeByContextName(contextName: string) {
    return this.getContext(contextName).pageSize;
  }

  getCurrentPageByContextName(contextName: string) {
    return this.getContext(contextName).currentPage;
  }

  canIncreasePage(contextName: string): boolean {
    let context = this.getContext(contextName);
    let bounds: Bounds = this.itemsThatAreOnGivenPage(context.currentPage, context.pageSize);
    /**
     * -1 on right
     * because upperBound is arraylike (counter from 0 upwards) and
     * itemsTotal is normal "number" of items
     */
    return bounds.upperBound < (context.itemsTotal - 1);
  }

  canDecreasePage(contextName: string): boolean {
    return this.getContext(contextName).currentPage > 0;
  }

  increasePage(contextName: string) {
    if (this.canIncreasePage(contextName)) {
      let context = this.getContext(contextName);
      context.currentPage++;
      this.paginationChanged.next(context);
    }
  }

  decreasePage(contextName: string) {
    if (this.canDecreasePage(contextName)) {
      let context = this.getContext(contextName);
      context.currentPage--;
      this.paginationChanged.next(context);
    }
  }

  setPage(contextName: string, pageNo: number) {
    if (pageNo >= 0 && pageNo <= this.maxPageForContext(contextName)) {
      let context = this.getContext(contextName);
      context.currentPage = pageNo;
      this.paginationChanged.next(context);
    }
  }

  setItemsNo(contextName: string, itemsNo: number) {
    if (itemsNo != null && itemsNo >= 0) {
      let context = this.getContext(contextName);
      context.itemsTotal = itemsNo;
      this.paginationChanged.next(context);
    }
  }

  /**
   * Used for more verbose counting, for given page number and page size
   * returns lower and upper bounds of items that are on this page
   */
  itemsThatAreOnGivenPage(pageNo: number, pageSize: number): Bounds {
    return {lowerBound: pageNo * pageSize, upperBound: pageNo * pageSize + pageSize - 1};
  }

  maxPageForContext(contextName: string) {
    let context = this.getContext(contextName);
    return this.maxPageForContextObject(context);
  }

  maxPageForContextObject(context: PaginationContext) {
    return Math.floor((context.itemsTotal - 1) / context.pageSize);
  }

  changePageSizeForContext(contextName: string, pageSize: number) {
    if (pageSize > 0) {
      let context = this.getContext(contextName);
      context.pageSize = pageSize;
      context.currentPage = 0;
    } else {
      throw {message: 'Requested changing to wrong page size!'};
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
