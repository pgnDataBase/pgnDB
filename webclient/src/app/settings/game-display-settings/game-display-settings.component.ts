import { Component, OnInit } from '@angular/core';
import {SettingsService} from '../settings.service';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';

@Component({
  selector: 'chessdb-game-display-settings',
  templateUrl: './game-display-settings.component.html',
  styleUrls: ['./game-display-settings.component.scss']
})
export class GameDisplaySettingsComponent implements OnInit {

  constructor(private settings: SettingsService, private modal: ModalMessageService) { }

  // MODELED SETTINGS
  moveFontSize: number;
  pieceFontSize: number;
  pieceDisplay: 'TEXT' | 'VISUAL' = 'TEXT';
  coloredVariantBorders: boolean;
  commentsAlwaysVisible: boolean;

  ngOnInit() {
    this.moveFontSize = this.settings.settingsValues.VISUAL_moveFontSize;
    this.pieceFontSize = this.settings.settingsValues.VISUAL_pieceFontSize;
    this.pieceDisplay = (this.settings.settingsValues.VISUAL_displayImagePieces) ? 'VISUAL' : 'TEXT';
    this.coloredVariantBorders = this.settings.settingsValues.VISUAL_coloredVariantBorders;
    this.commentsAlwaysVisible = this.settings.settingsValues.VISUAL_commentsAlwaysVisible;
  }

  clickedSave() {
    this.settings.settingsValues.VISUAL_pieceFontSize = this.pieceFontSize;
    this.settings.settingsValues.VISUAL_moveFontSize = this.moveFontSize;
    this.settings.settingsValues.VISUAL_displayImagePieces = this.pieceDisplay === 'VISUAL';
    this.settings.settingsValues.VISUAL_coloredVariantBorders = this.coloredVariantBorders;
    this.settings.settingsValues.VISUAL_commentsAlwaysVisible = this.commentsAlwaysVisible;
    this.settings.saveSettings();
  }

  changeDisplayOfPieces(visual: boolean) {
    this.pieceDisplay = visual ? 'VISUAL' : 'TEXT';
  }
}
