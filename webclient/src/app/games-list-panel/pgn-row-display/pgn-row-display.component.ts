import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ServerService} from '../../../services/backend-communication/server.service';
import {Router} from '@angular/router';
import {ROUTING_PATHS} from '../../../routing/routing-paths';
import {GameStateReactorService} from '../../../services/state-holders/game-state-reactor.service';
import {ChessGameMetadata} from '../../model/ChessGameMetadata';
import {ChessGame} from '../../model/ChessGame';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';

export class GameCheckEvent {
  checked: boolean;
  gameId: number;
}

@Component({
  selector: 'chessdb-pgn-row-display',
  templateUrl: './pgn-row-display.component.html',
  styleUrls: ['./pgn-row-display.component.scss']
})
export class PgnRowDisplayComponent implements OnInit {

  constructor(private server: ServerService,
              private router: Router,
              private gameState: GameStateReactorService,
              private modalMessage: ModalMessageService
  ) { }

  @Input()
  fixedValues = null;
  @Input()
  game: ChessGameMetadata;
  @Output()
  checkedEmitter: EventEmitter<GameCheckEvent> = new EventEmitter<GameCheckEvent>();
  @Output()
  actionSelectedEmitter: EventEmitter<string> = new EventEmitter();



  ngOnInit() {
  }

  clickedOnRow() {
    this.server.getGame(this.game.gameId).then((response) => {
      this.gameState.currentGame = response as ChessGame;
      this.router.navigate([ROUTING_PATHS.CHESSBOARD_VIEWER]);
    }).catch((err) => {
      this.modalMessage.openModalMessageOnChessDBException(err.error);
    })
  }

  checkedRow($event, gameId) {
    $event.stopPropagation();
    this.checkedEmitter.next({checked: $event.target.checked, gameId: gameId});
  }
}
