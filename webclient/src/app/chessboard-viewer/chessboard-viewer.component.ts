import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {LibloaderService} from '../../services/library-related/libloader.service';
import {ServerService} from '../../services/backend-communication/server.service';
import {ChessboardService} from '../../services/library-related/chessboard.service';
import {GameStateReactorService} from '../../services/state-holders/game-state-reactor.service';
import {Subscription} from 'rxjs';
import {ChessGame} from '../model/ChessGame';
import {Move} from '../model/Move';
import {Constants} from '../model/Constants';
import {GameEditionService} from './edition/game-edition.service';
import {ChessboardMove} from './edition/ChessboardMove';
import {TreeViewerComponent} from './tree-viewer/tree-viewer.component';

declare var ChessBoard: any;

const START_FEN = Constants.START_FEN;

@Component({
  selector: 'chessdb-chessboard-viewer',
  templateUrl: './chessboard-viewer.component.html',
  styleUrls: ['./chessboard-viewer.component.scss']
})
export class ChessboardViewerComponent implements OnInit, OnDestroy {

  constructor(private libloader: LibloaderService,
              private server: ServerService,
              private chessboardService: ChessboardService,
              private gameEdition: GameEditionService,
              private gameState: GameStateReactorService) { }

  // actual display of the game
  board: any;
  currentGame: ChessGame = null;
  canDecrease: boolean = true;
  canEnlarge: boolean = true;
  currentMove: Move = null;
  treeViewerVisible: boolean = false;
  treeInitialMove: Move = null;

  viewerChessboardConfig = {
    draggable: true,
    dropOffBoard: 'snapback',
    position: 'start',
    onDrop: this.moveHappened.bind(this)
  };

  @ViewChild(TreeViewerComponent)
  treeViewer: TreeViewerComponent;

  private subscription: Subscription = new Subscription();

  ngOnInit() {
    this.loadChessBoard();
    this.initializeSubscriptions();
  }

  loadChessBoard() {
    this.updateCanEnlargeDecrease();
    this.libloader.loadChessBoardJs();
    let checkExist = setInterval(() => {
      if (typeof ChessBoard !== 'undefined') {
        // js is already loaded
        clearInterval(checkExist);
        this.board = ChessBoard('board', this.viewerChessboardConfig);
        this.initializeBoardAccordingToState();
      }
    }, 100);
  }

  initializeSubscriptions() {
    if (this.gameState.currentGameChanges == null) return;
    let sub = this.gameState.currentGameChanges.subscribe((game) => {
      this.currentGame = game;
    });
    this.subscription.add(sub);
  }

  initializeBoardAccordingToState() {
    if (this.gameState.currentGame == null) return;
    this.currentGame = this.gameState.currentGame;
  }

  /**
   * Handle enlarging and decreasing size of chessboard -> mysle ze to jest do wywalenia @TODO
   */

  clickedEnlarge() {
    this.chessboardService.enlargeChessboard();
    this.board.resize();
    this.updateCanEnlargeDecrease();
  }

  clickedDecrease() {
    this.chessboardService.decreaseChessboard();
    this.board.resize();
    this.updateCanEnlargeDecrease();
  }

  updateCanEnlargeDecrease() {
    this.canDecrease = this.chessboardService.canDecrease();
    this.canEnlarge = this.chessboardService.canEnlarge();
  }

  setFenMoveOnBoard(fenMove: string) {
    if (fenMove != null) {
      this.board.position(fenMove);
    }
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  fakeStartMove(): Move {
    let move = new Move();
    move.fen = Constants.START_FEN;
    return move;
  }

  onCurrentMoveChanged(move: Move) {
    if (move != null) {
      this.currentMove = move;
      this.setFenMoveOnBoard(move.fen);
      this.updateTree(move);
    } else {
      this.currentMove = move;
      this.setFenMoveOnBoard(START_FEN);
      this.updateTree(this.fakeStartMove());
    }
  }

  updateTree(move: Move) {
    if (this.treeViewer != null) {
      this.treeViewer.refreshToMove(move);
    }
  }

  flip() {
    this.board.flip();
  }

  moveHappened(source, target, piece, newPos, oldPos, orientation) {
    // newPos oldPos are objects with positions for fen do ChessBoard.objToFen(newPos)
    const chessboardMove = new ChessboardMove(source, target, piece, ChessBoard.objToFen(newPos),
      ChessBoard.objToFen(oldPos), orientation);
    const resultingMove = ChessboardMove.chessboardMoveToChessdbMove(chessboardMove);
    this.gameEdition.moveCreationAttemptHasHappened(this.currentGame, this.currentMove, resultingMove);
  }

  positionChangeFromTree(fen: string) {
    this.currentMove = null;
    this.setFenMoveOnBoard(fen);
  }
}
