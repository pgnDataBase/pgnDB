import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ModelStateService} from '../../services/state-holders/model-state.service';
import {Subscription} from 'rxjs';
import {ChessDB} from '../model/ChessDB';
import {GameCheckEvent} from './pgn-row-display/pgn-row-display.component';
import {ServerService} from '../../services/backend-communication/server.service';
import {DropdownAction} from '../common/action-dropdown/action-dropdown.component';
import {ModalMessageService} from '../common/modal-message/modal-message.service';
import {PaginationService} from '../../services/pagination/pagination.service';
import {ROUTING_PATHS} from '../../routing/routing-paths';
import {Router} from '@angular/router';
import {GameStateReactorService} from '../../services/state-holders/game-state-reactor.service';
import {ChessGame} from '../model/ChessGame';
import {ChessGameMetadata} from '../model/ChessGameMetadata';

@Component({
  selector: 'chessdb-games-list-panel',
  templateUrl: './games-list-panel.component.html',
  styleUrls: ['./games-list-panel.component.scss']
})
export class GamesListPanelComponent implements OnInit, OnDestroy {

  games = null;
  db: ChessDB = null;
  checkedIds: Set<number> = new Set();

  gamesActions: DropdownAction[] = [
    {name: 'DELETE', display: 'Delete', icon: 'fa-trash', disabled: true},
    {name: 'EXPORT', display: 'Export', icon: 'fa-download', disabled: true},
    {name: 'NEW', display: 'New', icon: 'fa-plus', disabled: false}
  ];

  private subscription: Subscription = new Subscription();

  constructor(private state: ModelStateService,
              private modal: ModalMessageService,
              private modelState: ModelStateService,
              private gameState: GameStateReactorService,
              private pagination: PaginationService,
              private router: Router,
              private server: ServerService) {
  }

  ngOnInit() {
    this.db = this.state.currentDB;
    this.games = this.state.currentGamesMetadata;
    this.initializeSubscriptions();
  }

  initializeSubscriptions() {
    let sub = this.state.currentDBSubject.subscribe((db) => {
      this.db = db;
    });
    let sub2 = this.state.currentGamesMetadataSubject.subscribe((chessGames) => {
      /**
       * HACK na to zeby wyswietlal sie od razu spinner
       */
      setTimeout(() => {
        this.games = chessGames;
      }, 50);
    });
    this.subscription.add(sub);
    this.subscription.add(sub2);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  handleGameCheckEvent($event: GameCheckEvent) {
    if (this.checkedIds.has($event.gameId) && $event.checked === false) {
      this.checkedIds.delete($event.gameId);
    }
    if (!this.checkedIds.has($event.gameId) && $event.checked === true) {
      this.checkedIds.add($event.gameId);
    }
    this.setDisabledOrEnabledOnDeletingAndExporting();
  }

  setDisabledOrEnabledOnDeletingAndExporting() {
    let deletingAction = this.gamesActions.find(action => action.name === 'DELETE');
    deletingAction.disabled = this.checkedIds.size === 0;
    let exportingAction = this.gamesActions.find(action => action.name === 'EXPORT');
    exportingAction.disabled = this.checkedIds.size === 0;
  }

  handleActionSelected(actionName: string) {
    if (actionName === 'DELETE') {
      this.modelState.setAndEmitCurrentGames(null);
      this.server.deleteGames(Array.from(this.checkedIds), this.db.name).then((res) => {
        this.checkedIds.clear();
        this.setDisabledOrEnabledOnDeletingAndExporting();
        this.server.refreshAfterGamesDeletion();
      }).catch((err) => {
        this.modal.openModalMessageOnChessDBException(err.error);
      });
    }
    else if (actionName === 'EXPORT') {
      this.server.exportGames(this.db.name, Array.from(this.checkedIds));
    } else if (actionName === 'NEW') {
      this.createNewGame();
    }
  }

  createNewGame() {
    let newGame = ChessGame.emptyGame();
    this.gameState.currentGame = newGame;
    this.router.navigate([ROUTING_PATHS.CHESSBOARD_VIEWER]);
  }
}
