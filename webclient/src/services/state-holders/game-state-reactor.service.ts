import { Injectable } from '@angular/core';
import {ChessGame} from '../../app/model/ChessGame';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GameStateReactorService {

  currentGame : ChessGame = null;
  currentGameChanges: Subject<ChessGame> = new Subject<ChessGame>();

  constructor() {
  }
}
