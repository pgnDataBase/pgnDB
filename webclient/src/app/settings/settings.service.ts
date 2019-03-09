import {EventEmitter, Injectable} from '@angular/core';
import {ServerService} from '../../services/backend-communication/server.service';
import {ModalMessageService} from '../common/modal-message/modal-message.service';
import {PaginationService} from '../../services/pagination/pagination.service';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  constructor(private server: ServerService,
              private pagination: PaginationService,
              private modal: ModalMessageService) {
  }

  settingsValues = this.getDefaults();

  getDefaults() {
    return {
      GENERIC_copyFenOnMove: false,
      GENERIC_movePageSize: 800,
      GENERIC_gameListPageSize: 10,
      VISUAL_moveFontSize: 10,
      VISUAL_pieceFontSize: 18,
      VISUAL_displayImagePieces: true,
      VISUAL_coloredVariantBorders: true,
      VISUAL_commentsAlwaysVisible: false
    };
  }

  loadSettings() {
    this.settingsValues = this.getDefaults();
    this.server.getSettings()
      .then((res) => {
        if (res != null) {
          for (let key of Object.keys(res)) {
            // there were strange things with typing, this is workaround
            if (res[key] === 'true') {
              this.settingsValues[key] = true;
            } else if (res[key] === 'false') {
              this.settingsValues[key] = false;
            } else if (this.isNumeric(res[key])) {
              this.settingsValues[key] = parseInt(res[key]);
            } else {
              this.settingsValues[key] = res[key];
            }
          }
        }
        this.pagination.changePageSizeForContext('games', this.settingsValues.GENERIC_gameListPageSize);
        this.pagination.changePageSizeForContext('entities', this.settingsValues.GENERIC_movePageSize);
        SettingsService.settingsLoadEnd.next();
      }).catch(err => {
      this.settingsValues = this.getDefaults();
      SettingsService.settingsLoadEnd.next();
      this.modal.openModalMessage(
        'Server did not load settings', 'Assuming default settings.');
    });
  }

  saveSettings() {
    this.server.saveSettings(this.settingsValues)
      .then(res => this.modal.openModalMessage('Settings saved'))
      .catch(err => this.modal.openModalMessageOnChessDBException(err.error));
  }

  isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
  }

  public static settingsLoadEnd: EventEmitter<void> = new EventEmitter();
}
