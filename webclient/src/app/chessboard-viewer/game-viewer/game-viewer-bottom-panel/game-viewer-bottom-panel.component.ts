import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {GameEditionService} from '../../edition/game-edition.service';
import {Subscription} from 'rxjs';
import {ChessDBException} from '../../../model/ChessDBException';
import {ServerService} from '../../../../services/backend-communication/server.service';
import {ChessGame} from '../../../model/ChessGame';
import {ModalMessageService} from '../../../common/modal-message/modal-message.service';
import {GameHeadersViewerComponent} from '../../game-headers-viewer/game-headers-viewer.component';

@Component({
  selector: 'chessdb-game-viewer-bottom-panel',
  templateUrl: './game-viewer-bottom-panel.component.html',
  styleUrls: ['./game-viewer-bottom-panel.component.scss']
})
export class GameViewerBottomPanelComponent implements OnInit {

  constructor(private editor: GameEditionService,
              private modal: ModalMessageService,
              private server: ServerService) { }

  private subscription: Subscription = new Subscription();
  validationException: ChessDBException;
  visibilityInterval = null;
  opacityOfErrorMessage = 1;
  @Input()
  currentGame: ChessGame;
  @Input()
  treeVisible: boolean = false;
  @Output()
  treeVisibleChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  saving: boolean = false;

  ngOnInit() {
    let sub = this.editor.moveValidationErrorEvents.subscribe(exception => {
      this.handleMoveException(exception);
    });
    this.subscription.add(sub);
  }

  handleMoveException(exception: ChessDBException) {
    this.validationException = exception;
    this.opacityOfErrorMessage = 1;
    if (this.visibilityInterval != null) {
      clearInterval(this.visibilityInterval);
    }

    this.visibilityInterval = setInterval(() => {
      this.opacityOfErrorMessage -= 0.02;
      if (this.opacityOfErrorMessage <= 0.05) {
        clearInterval(this.visibilityInterval);
        this.validationException = null;
      }
    }, 100);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    if (this.visibilityInterval != null) {
      clearInterval(this.visibilityInterval);
    }
  }

  clickedSaveGame() {
    this.saving = true;
    if (this.currentGame.notPersisted) {
      this.currentGame.chessGameMetadata = GameHeadersViewerComponent.lastHeadersUpdatedState;
    }

    this.server.saveGame(this.currentGame)
      .then((res) => {
        this.currentGame.notPersisted = false;
        this.currentGame.chessGameMetadata.gameId = res.gameId;
        this.saving = false;
        this.server.refreshCurrentDb();
        this.modal.openModalMessage('Success', 'Changes saved');
      })
      .catch((err) => {
        this.saving = false;
        this.modal.openModalMessageOnChessDBException(err.error)
      })
  }

}
