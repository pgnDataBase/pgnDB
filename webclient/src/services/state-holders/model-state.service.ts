import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';
import {ChessDB} from '../../app/model/ChessDB';
import {ChessGameMetadata} from '../../app/model/ChessGameMetadata';

@Injectable({
  providedIn: 'root'
})
export class ModelStateService {

  // contains current games list state
  currentGamesMetadata : ChessGameMetadata[] = null;
  // notifies about current games changes
  currentGamesMetadataSubject: Subject<ChessGameMetadata[]> = new Subject<ChessGameMetadata[]>();

  public setAndEmitCurrentGames(currentGamesMetadata: ChessGameMetadata[]) {
    this.currentGamesMetadata = currentGamesMetadata;
    this.currentGamesMetadataSubject.next(currentGamesMetadata);
  }

  // contains currently selected database
  currentDB : ChessDB = null;
  // notifies about currently selected db changes
  currentDBSubject: Subject<ChessDB> = new Subject<ChessDB>();

  isExportingFileSubject: Subject<boolean> = new Subject<boolean>();

  public setAndEmitcurrentDB(currentDB: ChessDB) {
    this.currentDB = currentDB;
    this.currentDBSubject.next(currentDB);
  }

  //contains current DB list
  currentDatabases : ChessDB[] = null;
  //notifies about current DB list changes
  currentDatabasesChanges : Subject<ChessDB[]> = new Subject<ChessDB[]>();
  //notifies about particular DB change
  singleDatabaseChanges: Subject<ChessDB> = new Subject<ChessDB>();

  public setAndEmitcurrentDatabases(currentDatabases: ChessDB[]) {
    this.currentDatabases = currentDatabases;
    this.currentDatabasesChanges.next(currentDatabases);
  }

  constructor() { }

  reset() {
    this.currentDB = null;
    this.currentDatabases = null;
    this.currentGamesMetadata = null;
  }
}
