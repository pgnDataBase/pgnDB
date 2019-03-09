import { Component, OnInit } from '@angular/core';
import {SettingsService} from '../settings.service';
import {ServerService} from '../../../services/backend-communication/server.service';
import {ModelStateService} from '../../../services/state-holders/model-state.service';
import {PaginationService} from '../../../services/pagination/pagination.service';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';
import {Router} from '@angular/router';

@Component({
  selector: 'chessdb-generic-settings',
  templateUrl: './generic-settings.component.html',
  styleUrls: ['./generic-settings.component.scss']
})
export class GenericSettingsComponent implements OnInit {

  constructor(
    private server: ServerService,
    private modelState: ModelStateService,
    private pagination: PaginationService,
    private modalMessage: ModalMessageService,
    private router: Router,
    private settings: SettingsService) { }

  // MODELED SETTINGS
  pageSize: number;
  movePageSize: number;
  copyFenOnMove: boolean;

  ngOnInit() {
    this.pageSize = this.settings.settingsValues.GENERIC_gameListPageSize;
    this.movePageSize = this.settings.settingsValues.GENERIC_movePageSize;
    this.copyFenOnMove = this.settings.settingsValues.GENERIC_copyFenOnMove;
  }

  changePageSize() {
    try {
      this.pagination.changePageSizeForContext('games', this.pageSize);
      this.settings.settingsValues.GENERIC_gameListPageSize = this.pageSize;
    } catch(e) {
      this.modalMessage.openModalMessage('Operation failed', e.message);
    }
  }

  changeMovePageSize() {
    try {
      this.pagination.changePageSizeForContext('entities', this.movePageSize);
      this.settings.settingsValues.GENERIC_movePageSize = this.movePageSize;
    } catch(e) {
      this.modalMessage.openModalMessage('Operation failed', e.message);
    }
  }

  clickedSave() {
    this.changePageSize();
    this.changeMovePageSize();
    this.settings.settingsValues.GENERIC_copyFenOnMove = this.copyFenOnMove;
    this.settings.saveSettings();
  }
}
