import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {ChessGame} from '../../model/ChessGame';
import {Move} from '../../model/Move';

@Component({
  selector: 'chessdb-move-action-panel',
  templateUrl: './move-action-panel.component.html',
  styleUrls: ['./move-action-panel.component.scss']
})
export class MoveActionPanelComponent implements OnInit, OnChanges {

  /**
   * If currentMove is null this represent situation where no move was selected
   *
   * We can go forward (>) only in "given variant" (or "null" variant)
   *
   * We can go backward (<) in given variant and if not possible out of this variant
   */

  constructor() { }

  @Input()
  currentMoves: Move[];
  @Input()
  currentMove: Move;
  @Output()
  currentMoveChanged: EventEmitter<Move> = new EventEmitter();

  ngOnInit() {
    this.updateCanMoveBooleans();
  }

  ngOnChanges() {
    if (this.currentMoves) {
      this.updateCanMoveBooleans();
    }
  }

  canMoveToStartMove: boolean = true;
  canMoveForward: boolean = true;
  canMoveBackward: boolean = true;
  canMoveToEndMove: boolean = true;

  setNotMovesAllowed() {
    this.canMoveToStartMove = false;
    this.canMoveForward = false;
    this.canMoveToEndMove = false;
    this.canMoveBackward = false;
  }

  updateCanMoveBooleans() {
    if (this.currentMoves.length == 0) {
      this.setNotMovesAllowed();
      return;
    }
    let indexOfCurrentMove = this.indexOfCurrentMove();
    this.canMoveToStartMove = indexOfCurrentMove != null;
    this.canMoveBackward = indexOfCurrentMove != null;
    this.canMoveToEndMove = indexOfCurrentMove != this.currentMoves.length - 1;
    if (this.currentMove == null) {
      this.canMoveForward = true;
    } else if (this.currentMove.variantId == null) {
      this.canMoveForward = (indexOfCurrentMove !== this.currentMoves.length - 1);
    } else {
      let movesWithSameVariantId = this.movesWithSameVariantId(this.currentMove);
      let indexOnThisVariant = movesWithSameVariantId.findIndex(move => move === this.currentMove);
      if (indexOnThisVariant == movesWithSameVariantId.length - 2) {
        this.canMoveForward = false;
      } else {
        this.canMoveForward = true;
      }
    }
  }

  clickedToStartMove() {
    this.changeToMove(null);
  }

  clickedToPrevMove() {
    if (this.currentMove == null) {
      return;
    } else if (this.indexOfCurrentMove() == 0) {
      this.clickedToStartMove();
    } else {
      let movesWithSameVariantId = this.movesWithSameVariantId(this.currentMove);
      let indexOnThisVariant = movesWithSameVariantId.findIndex(move => move === this.currentMove);
      // not first in given variant -> move back in given variant
      if (indexOnThisVariant > 0) {
        this.changeToMove(movesWithSameVariantId[indexOnThisVariant - 1]);
      } else {
        // first in this variant -> step out of it
        let currIndex = this.indexOfCurrentMove();
        let prevMove = this.currentMoves[currIndex - 1];
        this.changeToMove(prevMove);
      }
    }
  }

  clickedToNextMove() {
    if (this.currentMove == null) {
      this.changeToFirstMoveInGame();
      return;
    }
    let movesWithSameVariantId = this.movesWithSameVariantId(this.currentMove);
    let indexOnThisVariant = movesWithSameVariantId.findIndex(move => move === this.currentMove);
    if (indexOnThisVariant < movesWithSameVariantId.length - 1) {
      this.changeToMove(movesWithSameVariantId[indexOnThisVariant + 1]);
    }
  }

  clickedToEndMove() {
    let lastMove = this.currentMoves[this.currentMoves.length - 1];
    this.changeToMove(lastMove);
  }

  changeToFirstMoveInGame() {
    this.changeToMove(this.currentMoves[0]);
  }

  indexOfMove(move: Move): number {
    return this.currentMoves.findIndex(mv => mv === move);
  }

  indexOfCurrentMove() {
    if (this.currentMove == null) {
      return null;
    }
    return this.indexOfMove(this.currentMove);
  }

  changeToMove(move: Move) {
    this.currentMove = move;
    this.updateCanMoveBooleans();
    this.currentMoveChanged.next(move);
  }

  movesWithSameVariantId(move: Move) {
    if (move == null) {
      return this.currentMoves.filter(move => move.variantId === null);
    }
    return this.currentMoves.
      filter(move => move.variantId === this.currentMove.variantId);
  }
}
