import {Component, OnDestroy, OnInit} from '@angular/core';
import {ModelStateService} from '../../services/state-holders/model-state.service';
import {ChessDB} from '../model/ChessDB';
import {Subscription} from 'rxjs';
import {ServerService} from '../../services/backend-communication/server.service';
import {ModalMessageService} from '../common/modal-message/modal-message.service';
import {Router} from '@angular/router';
import {ROUTING_PATHS} from '../../routing/routing-paths';
import {AuthenticationService} from '../authorization/authentication.service';

@Component({
  selector: 'chessdb-database-edit',
  templateUrl: './database-edit.component.html',
  styleUrls: ['./database-edit.component.scss']
})
export class DatabaseEditComponent implements OnInit, OnDestroy {

  database: ChessDB;
  private subscription: Subscription = new Subscription();
  loading: boolean = false;
  loadingFileExport: boolean = false;
  loadingDeletion: boolean = false;
  loadingAccessRemoval: boolean = false;
  isOwner: boolean = true;

  constructor(private model: ModelStateService,
              private server: ServerService,
              private router: Router,
              private auth: AuthenticationService,
              private modalMessage: ModalMessageService) { }

  ngOnInit() {
    this.database = this.model.currentDB;
    this.isOwner = this.auth.loggedInAs === this.database.ownerUsername;
    let sub = this.model.currentDBSubject.subscribe((db) => {
      this.database = db;
      this.isOwner = this.auth.loggedInAs === this.database.ownerUsername;
    });
    let sub2 = this.model.isExportingFileSubject
      .subscribe(state => this.loadingFileExport = state);
    this.subscription.add(sub);
    this.subscription.add(sub2);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  async databaseDeleteClicked() {
    this.loadingDeletion = true;
    await this.server.deleteDatabase(this.database.name).then((res) => {
      this.server.downloadAndRefreshDatabases();
      this.model.setAndEmitcurrentDB(null);
      this.model.setAndEmitCurrentGames(null);
      this.router.navigate([ROUTING_PATHS.GAMES_LIST]);
    }).catch((err) => {
      this.modalMessage.openModalMessageOnChessDBException(err.error);
    });
    this.loadingDeletion = false;
  }

  exportDatabaseClicked() {
    this.server.exportGames(this.database.name);
  }

  async removeOwnAccess() {
    this.loadingAccessRemoval = true;
    await this.server.removeDbPermission(this.database.id, this.auth.loggedInAs)
      .then(res => {
        this.model.reset();
        this.router.navigate([ROUTING_PATHS.DEFAULT]);
        this.server.downloadAndRefreshDatabases();
    }).catch(err => {
      this.modalMessage.openModalMessageOnChessDBException(err.error);
    });
    this.loadingAccessRemoval = false;
  }

}
