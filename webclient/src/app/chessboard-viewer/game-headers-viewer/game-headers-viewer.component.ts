import {Component, OnChanges, OnDestroy, OnInit} from '@angular/core';
import {GameStateReactorService} from '../../../services/state-holders/game-state-reactor.service';
import {Subscription} from 'rxjs';
import {ChessGame} from '../../model/ChessGame';
import {ServerService} from '../../../services/backend-communication/server.service';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';
import {ChessGameMetadata} from '../../model/ChessGameMetadata';
import {ModelStateService} from '../../../services/state-holders/model-state.service';
import {BasicHeaderKeys} from '../../model/BasicHeaderKeys';


@Component({
  selector: 'chessdb-game-headers-viewer',
  templateUrl: './game-headers-viewer.component.html',
  styleUrls: ['./game-headers-viewer.component.scss']
})
export class GameHeadersViewerComponent implements OnInit, OnDestroy {

  state: 'DISPLAY' | 'EDIT' | 'LOADING' = 'DISPLAY';
  editState: 'BASIC' | 'ADDITIONAL' = 'BASIC';

  basicHeaderKeys = BasicHeaderKeys.keys;

  public static lastHeadersUpdatedState: ChessGameMetadata = new ChessGameMetadata();

  pgnHeader: ChessGameMetadata = {
    gameId: null,
    White: null,
    Black: null,
    Result: null,
    Site: null,
    Event: null,
    Round: null,
    additional: {}
  };

  editHeaders: ChessGameMetadata = {
    gameId: null,
    White: '',
    Black: '',
    Result: '',
    Site: '',
    Event: '',
    Round: '',
    additional: {}
  };

  currentGame: ChessGame;

  private subscription: Subscription = new Subscription();

  constructor(private gameState: GameStateReactorService,
              private server: ServerService,
              private modelState: ModelStateService,
              private modal: ModalMessageService) { }

  ngOnInit() {
    this.currentGame = this.gameState.currentGame;
    this.initialize();
    let sub = this.gameState.currentGameChanges.subscribe((game) => {
      this.currentGame = game;
      this.initialize();
    });
    this.subscription.add(sub);
  }

  initialize() {
    this.pgnHeader.Black = this.currentGame.chessGameMetadata.Black;
    this.pgnHeader.White = this.currentGame.chessGameMetadata.White;
    this.pgnHeader.Date = this.currentGame.chessGameMetadata.Date;
    this.pgnHeader.Event = this.currentGame.chessGameMetadata.Event;
    this.pgnHeader.Result = this.currentGame.chessGameMetadata.Result;
    this.pgnHeader.Site = this.currentGame.chessGameMetadata.Site;
    this.pgnHeader.gameId = this.currentGame.chessGameMetadata.gameId;
    this.pgnHeader.Round = this.currentGame.chessGameMetadata.Round;
    this.pgnHeader.additional = this.currentGame.chessGameMetadata.additional;
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  clickedInitEdit() {
    this.loadToEdit();
    this.state = 'EDIT';
  }

  clickedCancelEdit() {
    this.state = 'DISPLAY';
  }

  async clickedConfirmEdit() {
    // new game, save only locally
    if (this.currentGame.notPersisted) {
      this.refreshHeadersOnSuccessfulSave();
      this.state = 'DISPLAY';
      return;
    }
    this.state = 'LOADING';
    await this.server.editHeaders(this.modelState.currentDB.name, this.editHeaders).then((res) => {
      this.refreshHeadersOnSuccessfulSave();
    }).catch((err) => {
      this.modal.openModalMessageOnChessDBException(err.error);
    });
    this.state = 'DISPLAY';
  }

  loadToEdit() {
    this.editHeaders = this.headersCopy(this.pgnHeader);
  }

  refreshHeadersOnSuccessfulSave() {
    GameHeadersViewerComponent.lastHeadersUpdatedState = this.editHeaders;
    this.pgnHeader = this.headersCopy(this.editHeaders);
  }

  headersCopy(headers: ChessGameMetadata): ChessGameMetadata {
    return {
      gameId: headers.gameId,
      Event: headers.Event,
      White: headers.White,
      Black: headers.Black,
      Date: headers.Date,
      Round: headers.Round,
      Result: headers.Result,
      Site: headers.Site,
      additional: this.copyOfAdditional(headers)
    }
  }

  copyOfAdditional(headers: ChessGameMetadata) {
    if (headers.additional == null) {
      return {};
    } else {
      let result = {};
      Object.keys(headers.additional).forEach((key) => {
        result[key] = headers.additional[key];
      });
      return result;
    }
  }

  getAdditionalKeys(headers: ChessGameMetadata) {
    if (headers.additional == null) {
      return [];
    } else {
      return Object.keys(headers.additional);
    }
  }

  addHeadersState: 'INIT' | 'ADDING' = 'INIT';

  clickedAddMoreHeaders() {
    this.addHeadersState = 'ADDING';
  }

  addNewHeader(headerName) {
    if (!this.isHeaderAlreadyDefined(this.editHeaders, headerName)) {
      this.editHeaders.additional[headerName] = '';
      this.addHeadersState = 'INIT';
    } else {
      this.modal.openModalMessage('This header is already defined!');
    }
  }

  deleteAdditionalHeader(additionalHeaderKey) {
    delete this.editHeaders.additional[additionalHeaderKey];
  }

  isHeaderAlreadyDefined(headers: ChessGameMetadata, headerName: string) {
    if (headers == null) return false;
    for (let key of this.basicHeaderKeys) {
      if (key == headerName)
        return true;
    }
    if (headers.additional == null) return false;
    for (let key of Object.keys(headers.additional)) {
      if (key == headerName)
        return true;
    }
    return false;
  }

  flipEditBasicOrAdditional() {
    if (this.editState === 'BASIC') {
      this.editState = 'ADDITIONAL';
    } else {
      this.editState = 'BASIC';
    }
  }

  getMaxLengthForKey(key: string) {
    if (key === 'White') {
      return 100;
    } else if (key === 'Black') {
      return 50;
    } else if (key === 'Result') {
      return 10;
    } else if (key === 'Site') {
      return 50;
    } else if (key === 'Event') {
      return 50;
    } else if (key === 'Round') {
      return 2;
    } else if (key === 'Date') {
      return 11;
    } else {
      // additional keys
      return 30;
    }
  }

  validForSave() {
    for (let basicKey of this.basicHeaderKeys) {
      if (this.editHeaders[basicKey] != null && this.editHeaders[basicKey].length > this.getMaxLengthForKey(basicKey)) {
        return false;
      }
    }
    if (this.editHeaders.additional != null) {
      for (let additionalKey of Object.keys(this.editHeaders.additional)) {
        if (this.editHeaders.additional[additionalKey].length > 30) {
          return false;
        }
      }
    }
    return true;
  }
}
