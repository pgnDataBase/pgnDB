import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService, AuthEventType} from '../authorization/authentication.service';
import {Router} from '@angular/router';
import {ServerService} from '../../services/backend-communication/server.service';
import {ModelStateService} from '../../services/state-holders/model-state.service';
import {ChessDB} from '../model/ChessDB';
import {ROUTING_PATHS} from '../../routing/routing-paths';
import {Subscription} from 'rxjs';
import {ModalMessageService} from '../common/modal-message/modal-message.service';
import {DbFilterService} from '../../services/db-filter.service';

@Component({
  selector: 'chessdb-left-side-panel',
  templateUrl: './left-side-panel.component.html',
  styleUrls: ['./left-side-panel.component.scss']
})
export class LeftSidePanelComponent implements OnInit, OnDestroy {

  constructor(private auth: AuthenticationService,
              private router: Router,
              private server: ServerService,
              private modelState: ModelStateService,
              private filter: DbFilterService,
              private modalMessage: ModalMessageService) { }

  chessDatabases : ChessDB[];
  showPanel: boolean = true;
  filtersActive = {};
  currentDb: ChessDB;
  isLoadingDatabases: boolean = false;
  loggedInAs: string;

  private subscription: Subscription = new Subscription();

  async ngOnInit() {
    this.loggedInAs = this.auth.loggedInAs;
    this.isLoadingDatabases = true;
    await this.server.getDatabaseList().then((response) => {
      this.chessDatabases = response as ChessDB[];
      this.modelState.currentDatabases = this.chessDatabases;
    }).catch((err) => {
      this.modalMessage.openModalMessageOnChessDBException(err.error);
    });
    this.isLoadingDatabases = false;
    let sub = this.modelState.currentDatabasesChanges.subscribe((dbs) => {
      this.chessDatabases = dbs;
    });
    let sub2 = this.modelState.singleDatabaseChanges.subscribe((changedDb: ChessDB) => {
      let modifiedDb = this.chessDatabases.find(database => database.id === changedDb.id);
      modifiedDb.name = changedDb.name;
      modifiedDb.gamesTotal = changedDb.gamesTotal;
    });
    let sub3 = this.filter.filteringChanged.subscribe(change => {
      this.filtersActive[change.dbName] = change.filtered;
    });
    this.subscription.add(sub);
    this.subscription.add(sub2);
    this.subscription.add(sub3);
  }

  clickedLogout() {
    this.auth.logoutAction();
  }

  clickedToList() {
    this.clickedDbInfo(this.modelState.currentDB);
  }

  clickedToSettings() {
    this.router.navigate([ROUTING_PATHS.SETTINGS]);
  }

  clickedDbInfo(database: ChessDB) {
    this.modelState.setAndEmitCurrentGames(null);
    this.modelState.setAndEmitcurrentDB(database);
    this.currentDb = database;
    this.router.navigate([ROUTING_PATHS.GAMES_LIST]).then(() => {
      this.server.downloadAndRefreshGames(database);
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  flipShowPanel() {
    this.showPanel = !this.showPanel;
  }

  clickedEditDatabase(database: ChessDB) {
    this.modelState.setAndEmitcurrentDB(database);
    this.router.navigate([ROUTING_PATHS.DB_EDIT]);
  }

  clickedFilterDatabase(database: ChessDB) {
    this.modelState.setAndEmitcurrentDB(database);
    this.router.navigate([ROUTING_PATHS.DB_FILTER]);
  }

}
