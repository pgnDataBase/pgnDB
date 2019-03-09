import {Component, Input, OnInit} from '@angular/core';
import {DatabaseAccessModifierService} from './database-access-modifier.service';
import {ChessDB} from '../../model/ChessDB';
import {ServerService} from '../../../services/backend-communication/server.service';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';
import {AuthenticationService} from '../../authorization/authentication.service';

@Component({
  selector: 'chessdb-database-access-modifier',
  templateUrl: './database-access-modifier.component.html',
  styleUrls: ['./database-access-modifier.component.scss']
})
export class DatabaseAccessModifierComponent implements OnInit {

  @Input()
  currentDatabase: ChessDB;
  usersAllowed: string[] = [];
  usersDenied: string[] = [];
  loggedInAs: string = null;
  performingAction: boolean = false;

  constructor(private databaseAccessModifier: DatabaseAccessModifierService,
              private auth: AuthenticationService,
              private modal: ModalMessageService,
              private server: ServerService) { }

  ngOnInit() {
    this.loggedInAs = this.auth.loggedInAs;
    this.setUsersAllowedAndDenied();
  }

  setUsersAllowedAndDenied() {
    this.server.usersThatHavePermissionToDb(this.currentDatabase.id)
      .then(res => {
        this.usersAllowed = res as string[];
        this.setInitialUsersDenied();
      }).catch(err => {
      this.modal.openModalMessageOnChessDBException(err.error);
    });
  }

  setInitialUsersDenied() {
    this.server.searchUsers('').then(res => {
      let allUsers = res as string[];
      this.usersDenied = allUsers.filter(user => {
        return user !== this.loggedInAs && !this.usersAllowed.includes(user)
      });
    }).catch(err => {
      this.modal.openModalMessageOnChessDBException(err.error);
    })
  }

  async addUserAccess(username) {
    this.performingAction = true;
    await this.server.giveDbPermission(this.currentDatabase.id, username).then(() => {
      this.usersAllowed.push(username);
      this.usersDenied.splice(this.usersDenied.findIndex(u => u === username), 1);
    }).catch(err => {
      this.modal.openModalMessageOnChessDBException(err.error);
    });
    this.performingAction = false;
  }

  async removeUserAccess(username) {
    this.performingAction = true;
    await this.server.removeDbPermission(this.currentDatabase.id, username).then(() => {
      this.usersDenied.push(username);
      this.usersAllowed.splice(this.usersAllowed.findIndex(u => u === username), 1);
    }).catch(err => {
      this.modal.openModalMessageOnChessDBException(err.error);
    });
    this.performingAction = false;
  }

}
