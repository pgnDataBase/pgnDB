import { Injectable } from '@angular/core';


/**
 * Service that is supposed to help with ways that
 * chessboard is DISPLAYED
 */
@Injectable({
  providedIn: 'root'
})
export class ChessboardService {

  constructor() { }

  chessboardConfigTrash = {
    draggable: true,
    dropOffBoard: 'trash',
    position: 'start'
  };

  currentSize: number = 500;
  changeStep: number = 50;
  minSize: number = 200;
  maxSize: number = 500;

  //enlarges and decreases only div, loading deloading and other logic done outside
  enlargeChessboard() {
    if (!this.canEnlarge()) return;
    let board = document.getElementById('board');
    this.currentSize = this.currentSize + this.changeStep;
    board.style.width = this.currentSize.toString() + "px";
    board.style.height = this.currentSize.toString() + "px";
  }

  decreaseChessboard() {
    if (!this.canDecrease()) return;
    let board = document.getElementById('board');
    this.currentSize = this.currentSize - this.changeStep;
    board.style.width = this.currentSize.toString() + "px";
    board.style.height = this.currentSize.toString() + "px";
  }

  canDecrease() : boolean {
    return this.currentSize > this.minSize;
  }

  canEnlarge() : boolean {
    return this.currentSize < this.maxSize;
  }
}
