import { Component, OnInit } from '@angular/core';
import {ServerService} from '../../../services/backend-communication/server.service';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';
import {AuthenticationService} from '../../authorization/authentication.service';
import {UserService} from '../../../services/backend-communication/user.service';

@Component({
  selector: 'chessdb-account-settings',
  templateUrl: './account-settings.component.html',
  styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {

  constructor(private userService: UserService,
              private auth: AuthenticationService,
              private modal: ModalMessageService) { }

  accountDeletionInitiated: boolean = false;

  ngOnInit() {
  }

  deleteAccount(password) {
    this.userService.deleteAccount(password).then(res => {
      this.auth.logoutWithoutHttp();
      this.modal.openModalMessage('Account deleted.',
        'Your pngDB account has been deleted');
    })
      .catch(err => this.modal.openModalMessageOnChessDBException(err.error));
  }

}
