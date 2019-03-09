import {Component, Input, OnDestroy} from '@angular/core';
import {ChessDB} from '../../model/ChessDB';
import {ServerService} from '../../../services/backend-communication/server.service';
import {ModelStateService} from '../../../services/state-holders/model-state.service';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'chessdb-database-merger',
  templateUrl: './database-merger.component.html',
  styleUrls: ['./database-merger.component.scss']
})
export class DatabaseMergerComponent implements OnDestroy {

  constructor(private server: ServerService,
              private modalMessage: ModalMessageService,
              private model: ModelStateService) {
    this.dbs = this.model.currentDatabases
      .filter(db => {
        return db.id !== this.model.currentDB.id;
      });
    let sub = this.model.currentDatabasesChanges.subscribe(dbs => {
      this.dbs = this.model.currentDatabases
        .filter(db => {
          return db.id !== this.model.currentDB.id;
        });
    });
    this.subscription.add(sub);
  }

  @Input()
  currentDatabase: ChessDB = null;
  dbs: ChessDB[];
  askingConfirmation: boolean = false;
  dbToBeMerged: ChessDB = null;
  isMerging: boolean = false;

  private subscription: Subscription = new Subscription();

 async confirmedMerge(db: ChessDB) {
    this.isMerging =true;
   this.askingConfirmation = false;
    await this.server.mergeDatabases(db.name, this.currentDatabase.name).then(res => {
      this.server.downloadAndRefreshDatabases();
      this.modalMessage.openModalMessage('Merge was successful.');
    }).catch(err => {
      this.modalMessage.openModalMessageOnChessDBException(err.error);
    });
    this.isMerging = false;
  }

  ngOnDestroy() {
   this.subscription.unsubscribe();
  }

}
