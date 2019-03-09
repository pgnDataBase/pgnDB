import { Component, OnInit } from '@angular/core';
import {ServerService} from '../../../services/backend-communication/server.service';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';

@Component({
  selector: 'chessdb-database-adder',
  templateUrl: './database-adder.component.html',
  styleUrls: ['./database-adder.component.scss']
})
export class DatabaseAdderComponent implements OnInit {

  state: 'INIT' | 'INPUT' | 'LOADING' = 'INIT';

  constructor(private server: ServerService, private modalMessage: ModalMessageService) { }

  ngOnInit() {

  }

  clickedAddDbInit() {
    this.state = 'INPUT';
  }

  clickedCancel() {
    this.reset();
  }

  clickedAddDb(dbName: string) {
    this.state = 'LOADING';
    this.server.addDatabase(dbName).then(() => {
      this.reset();
      this.server.downloadAndRefreshDatabases();
    }).catch((err) => {
      this.reset();
      this.modalMessage.openModalMessageOnChessDBException(err.error);
    })
  }

  reset() {
    this.state = 'INIT';
  }

}
