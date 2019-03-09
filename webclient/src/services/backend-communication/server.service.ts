import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ChessDB} from '../../app/model/ChessDB';
import {ModelStateService} from '../state-holders/model-state.service';
import {PaginationService} from '../pagination/pagination.service';
import {ChessGameMetadata} from '../../app/model/ChessGameMetadata';
import {ModalMessageService} from '../../app/common/modal-message/modal-message.service';
import {DbFilterService} from '../db-filter.service';
import {HeadersAndPositionFilter} from '../../app/model/HeadersAndPositionFilter';
import {saveAs} from '../../../node_modules/file-saver/dist/FileSaver.js';
import {Move} from '../../app/model/Move';
import {ChessGame} from '../../app/model/ChessGame';
import {TreeNodeRequest} from '../../app/model/tree/TreeNodeRequest';
import {Constants} from '../../app/model/Constants';

@Injectable({
  providedIn: 'root'
})
/**
 * Service which is base for communication with backend api regarding chessdb
 * @REFACTOR when its fully clarified partition into smaller services
 */
export class ServerService {

  baseUrl = 'http://'+ Constants.SERVER_IP + '/api';

  DATABASE_LIST_URL = '/chessdbs';
  GET_LIST_OF_GAMES_URL = '/games';
  GET_GAME_URL = '/game';

  constructor(private http: HttpClient,
              private modelState: ModelStateService,
              private modal: ModalMessageService,
              private filters: DbFilterService,
              private pagination: PaginationService) {
    Constants.serverIpChanged.subscribe(() => this.recalculateBaseUrl());
  }

  recalculateBaseUrl() {
    this.baseUrl = 'http://'+ Constants.SERVER_IP + '/api';
  }

  getDatabaseList() {
    return this.http.get(this.baseUrl + this.DATABASE_LIST_URL).toPromise();
  }

  getGamesMetadata(dbName: string) {
    let headAndPosFilter: HeadersAndPositionFilter =
      this.filters.getHeaderAndPositionFilterForDb(dbName);
    return this.http.post(this.baseUrl + this.GET_LIST_OF_GAMES_URL,
      {
        databaseName: dbName,
        offset: this.pagination.getOffsetByContextName('games'),
        pageSize: this.pagination.getPageSizeByContextName('games'),
        filter: headAndPosFilter
      },
    )
      .toPromise();
  }

  getGame(gameId: number) {
    return this.http.post(this.baseUrl + this.GET_GAME_URL + '/' + gameId,
      {
        databaseName: this.modelState.currentDB.name,
      },
    )
      .toPromise();
  }

  downloadAndRefreshGamesFromCurrentDB() {
    this.downloadAndRefreshGames(this.modelState.currentDB);
  }

  downloadAndRefreshGames(database: ChessDB) {
    this.modelState.currentGamesMetadata = null;
    this.modelState.currentGamesMetadataSubject.next(null);
    this.getGamesMetadata(database.name).then((response) => {
      let games = response['games'] as ChessGameMetadata[];
      let total = response['total'] as number;
      if (total != null) {
        this.pagination.getContext('games').itemsTotal = total;
      }
      this.modelState.currentGamesMetadataSubject.next(games);
      this.modelState.currentGamesMetadata = games;
    }).catch((err) => {
      this.modal.openModalMessageOnChessDBException(err.error);
    });
  }

  downloadAndRefreshDatabases() {
    this.getDatabaseList().then((dbs) => {
      this.modelState.currentDatabases = dbs as ChessDB[];
      this.modelState.currentDatabasesChanges.next(dbs as ChessDB[]);
    });
  };

  refreshCurrentDb() {
    this.getDatabaseList().then((dbs) => {
      let currDbRefreshed =
        (dbs as ChessDB[]).find(db => db.id === this.modelState.currentDB.id);
      this.modelState.currentDB = currDbRefreshed;
      this.modelState.singleDatabaseChanges.next(currDbRefreshed);
      this.pagination.getContext('games').itemsTotal = currDbRefreshed.gamesTotal;
      this.pagination.paginationChanged.next(this.pagination.getContext('games'));
    });
  }

  uploadPngFile(body, dbName: string): Promise<any> {
    return this.http.post(this.baseUrl + '/files/upload/' + dbName, body).toPromise();
  }

  deleteGames(gamesIds: number[], dbName: string): Promise<any> {
    return this.http.post(this.baseUrl + '/games/delete',
      {
        gamesIds: gamesIds,
        databaseName: dbName
      }).toPromise();
  }

  addDatabase(dbName: string): Promise<any> {
    return this.http.post(this.baseUrl + '/chessdb', {
      databaseName: dbName
    }).toPromise();
  }

  deleteDatabase(dbName: string): Promise<any> {
    return this.http.delete(this.baseUrl + '/chessdb', {
      params: {
        databaseName: dbName
      }
    }).toPromise();
  }

  editHeaders(dbName, headers: ChessGameMetadata): Promise<any> {
    return this.http.post(this.baseUrl + '/game/modify/', headers, {
      params: {
        databaseName: dbName
      }
    }).toPromise();
  }

  refreshAfterGamesDeletion() {
    this.pagination.setPage('games', 0);
    this.refreshCurrentDb();
    this.downloadAndRefreshGamesFromCurrentDB();
  }

  exportGames(dbName: string, gamesIds?: any[]) {
    this.modelState.isExportingFileSubject.next(true);

    let dateOfExport = new Date().toLocaleDateString("en-EN");
    const fileName = dbName + "-" + dateOfExport + '.pgn';
    let getAllGames = gamesIds == null;

    const headers = new HttpHeaders();
    headers.append('Accept', 'application/octet-stream');
    headers.append('Content-Type', 'application/octet-stream');
    this.http.post(this.baseUrl + '/games/export/',
      {
        fileName: 'doesnotmatter',
        databaseName: dbName,
        getAllGames: getAllGames,
        gamesIds: gamesIds
      }, {
        headers: headers,
        responseType: 'blob'
      })
      .toPromise().then(response => {
      saveAs(response, fileName);
      this.modelState.isExportingFileSubject.next(false);
    }).catch((err) => {
      this.modal.openModalMessageOnChessDBException(err.error);
      this.modelState.isExportingFileSubject.next(false);
    });
  }

  getAvailableHeaders(dbName, value): Promise<any> {
    return this.http.post(this.baseUrl + '/search/tags/', {
      databaseName: dbName,
      value: value
    }).toPromise();
  }

  resolveUploadStatus(dbName: string): Promise<any> {
    return this.http.post(this.baseUrl + '/status/loading', {
      databaseName: dbName
    }).toPromise();
  }

  getSettings(): Promise<any> {
    return this.http.get(this.baseUrl + '/user/settings').toPromise();
  }

  saveSettings(settings): Promise<any> {
    return this.http.post(this.baseUrl + '/user/settings', {
      settings: settings
    }).toPromise();
  }

  newMoveCreation(previousMove: Move, newMove: Move): Promise<any> {
    return this.http.post(this.baseUrl + '/move/create', {
      previousMove: previousMove,
      newMove: newMove
    }).toPromise();
  }

  saveGame(chessGame: ChessGame): Promise<any> {
    return this.http.post(this.baseUrl + '/moves/save', {
      chessGameData: chessGame,
      databaseName: this.modelState.currentDB.name
    }).toPromise();
  }

  buildTree(databaseName: string, includeVariants?: boolean): Promise<any> {
    return this.http.post(this.baseUrl + '/tree', {
      databaseName: databaseName,
      includeVariants: includeVariants
    }).toPromise();
  }

  getTreeNodes(treeNodeRequest: TreeNodeRequest): Promise<any> {
    return this.http.post(this.baseUrl + '/search/tree', treeNodeRequest).toPromise();
  }

  treeBuildingStatus(databaseName: string): Promise<any> {
    return this.http.post(this.baseUrl + '/status/building/tree', {
      databaseName: databaseName
    }).toPromise();
  }

  //db access
  // @TODO INVESTIGATE THE CORS ERRORS WHEN CALLING THESE METHODS FROM
  // DATABASE ACCESS MODIFIER COMPONENT THEN REMOVE THEM FROM HERE

  giveDbPermission(databaseId: string, username: string) {
    return this.http.post(this.baseUrl + '/chessdb/access', {
      databaseId: databaseId,
      username: username
    }).toPromise();
  }

  searchUsers(value: string) {
    return this.http.post(this.baseUrl + '/search/users', {
      value: value
    }).toPromise();
  }

  usersThatHavePermissionToDb(databaseId: string) {
    return this.http.post(this.baseUrl + '/chessdb/access/users', {
      databaseId: databaseId
    }).toPromise();
  }

  removeDbPermission(databaseId: string, username: string) {
    return this.http.post(this.baseUrl + '/chessdb/access/remove', {
      databaseId: databaseId,
      username: username
    }).toPromise();
  }


  // END OF TODO

  mergeDatabases(fromName: string, toName: string) {
    return this.http.post(this.baseUrl + '/chessdbs/merging', {
      from: fromName,
      to: toName
    }).toPromise();
  }

}
