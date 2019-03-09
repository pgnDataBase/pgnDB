import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {PaginationContext, PaginationService} from '../../../services/pagination/pagination.service';
import {ServerService} from '../../../services/backend-communication/server.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'chessdb-pagination-changer',
  templateUrl: './pagination-changer.component.html',
  styleUrls: ['./pagination-changer.component.scss']
})
export class PaginationChangerComponent implements OnInit, OnDestroy {

  constructor(
    private pagination: PaginationService,
    private server: ServerService
  ) {
  }

  canDecrease: boolean = false;
  canIncrease: boolean = false;
  currPage: number = 0;
  maxPage: number = 0;
  //input variable from user for page that he wants to directly access
  gotoPage: number = 1;

  private subscription: Subscription = new Subscription();

  clickedPageBack() {
    this.pagination.decreasePage('games');
    this.refreshCanIncreaseDecrease();
    this.refreshCurrMaxPage();
  }

  clickedPageForward() {
    this.pagination.increasePage('games');
    this.refreshCanIncreaseDecrease();
    this.refreshCurrMaxPage();
  }

  ngOnInit() {
    this.refreshCanIncreaseDecrease();
    this.refreshCurrMaxPage();
    let sub = this.pagination.paginationChanged.subscribe(
      (context: PaginationContext) => {
        if (context.contextName === 'games') {
          this.server.downloadAndRefreshGamesFromCurrentDB();
          this.refreshCurrMaxPage();
        }
      });
    this.subscription.add(sub);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  refreshCanIncreaseDecrease() {
    this.canDecrease = this.pagination.canDecreasePage('games');
    this.canIncrease = this.pagination.canIncreasePage('games');
  }

  refreshCurrMaxPage() {
    this.currPage = this.pagination.getCurrentPageByContextName('games');
    this.maxPage = this.pagination.maxPageForContext('games');
    // after going to certain page and refreshing show refreshed page on goto
    this.gotoPage = this.currPage + 1;
  }

  handleClickedGotoPage() {
    if (!this.canGoToPage(this.gotoPage)) return;
    let programaticPage = this.gotoPage - 1;
    this.pagination.setPage('games', programaticPage);
    this.refreshCanIncreaseDecrease();
    this.refreshCurrMaxPage();
  }

  canGoToPage(pageFromUserNo: number): boolean {
    // because user input is from 1 upwards and programatically pages are counted from 0 upwards
    let programaticPage = pageFromUserNo - 1;
    return programaticPage >= 0 && programaticPage <= this.maxPage;
  }

}
