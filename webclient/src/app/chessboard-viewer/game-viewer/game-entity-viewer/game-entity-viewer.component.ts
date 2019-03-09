import {Component, EventEmitter, HostBinding, Input, OnInit, Output} from '@angular/core';
import {Move} from '../../../model/Move';
import {NumberToColorMapperService} from '../../../../services/number-to-color-mapper.service';
import {SettingsService} from '../../../settings/settings.service';
import {PieceToUnicodeTranslationUtil} from './piece-to-unicode-translation-util';

@Component({
  selector: 'chessdb-game-entity-viewer',
  templateUrl: './game-entity-viewer.component.html',
  styleUrls: ['./game-entity-viewer.component.scss']
})
export class GameEntityViewerComponent implements OnInit {

  @Input()
  entity: Move;
  @Input()
  currentMove: Move = null;
  @HostBinding('style.flex-basis') basis: string = '1';
  @HostBinding('style.font-size.px') VISUAL_moveFontSize: number = 12;
  VISUAL_pieceFontSize;
  DISPLAY_coloredVariantBorders;
  VISUAL_commentsAlwaysVisible;

  @Input()
  variantDepth: number;
  @Input()
  hasMoreVariantsInside: boolean;

  variantColor: string = 'white';
  depthMargin: number = 30;
  displayImagePieces: boolean = false;
  translatePieceToUnicode = PieceToUnicodeTranslationUtil.pieceToUnicode;

  displayCommentEdition: boolean = false;

  constructor(private colorMapper: NumberToColorMapperService, private settings: SettingsService) { }

  ngOnInit() {
    if (this.settings.settingsValues.VISUAL_displayImagePieces != null) {
      this.displayImagePieces = this.settings.settingsValues.VISUAL_displayImagePieces;
    }
   // this.basis = this.entity.variantType === 'VB' ? '100%' : '1';
    this.variantColor = this.colorMapper.mapNumberToColor(this.entity.variantId);
    this.VISUAL_moveFontSize = this.settings.settingsValues.VISUAL_moveFontSize;
    this.VISUAL_pieceFontSize = this.settings.settingsValues.VISUAL_pieceFontSize;
    this.DISPLAY_coloredVariantBorders = this.settings.settingsValues.VISUAL_coloredVariantBorders;
    this.VISUAL_commentsAlwaysVisible = this.settings.settingsValues.VISUAL_commentsAlwaysVisible;
  }

  @Output()
  clickedOnEntityEmitter: EventEmitter<Move> = new EventEmitter();

  clickedOnEntity() {
    this.clickedOnEntityEmitter.next(this.entity);
  }

  translatePieceCodeToDisplay(pieceCode) {
    const color = pieceCode.substr(0, 1);
    const piece = pieceCode.substr(1, 2);
    const translatedColor = color === 'W' ? 'White' : 'Black';
    let translatedPiece;
    if (piece === 'PN') {
      translatedPiece = '';
    }
    if (piece === 'KT') {
      translatedPiece = 'N';
    }
    if (piece === 'BP') {
      translatedPiece = 'B';
    }
    if (piece === 'QN') {
      translatedPiece = 'Q';
    }
    if (piece === 'RK') {
      translatedPiece = 'R';
    }
    if (piece === 'KG') {
      translatedPiece = 'K';
    }
    return translatedPiece;
  }

  isMoveWhite(): boolean {
    if (this.entity.pieceCode == null) return false;
    const color = this.entity.pieceCode.substr(0, 1);
    return color === 'W';
  }

  getMoveNumberToDisplay() {
    if (this.isMoveWhite()) {
      return Math.ceil(this.entity.moveNumber / 2);
    } else if (!this.isMoveWhite() &&
      (this.entity.variantType === 'VB')) {
      return Math.ceil(this.entity.moveNumber / 2) + '...';
    }
    return '';
  }
  //t
}
