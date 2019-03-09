import {Component} from '@angular/core';
import {PiecePromotionService} from '../piece-promotion.service';
import {PieceToUnicodeTranslationUtil} from '../../../game-viewer/game-entity-viewer/piece-to-unicode-translation-util';
import {ModalSubjectsService} from '../../../../common/modal-message/modal-subjects.service';

@Component({
  selector: 'chessdb-promote-piece',
  templateUrl: './promote-piece.component.html',
  styleUrls: ['./promote-piece.component.scss']
})
export class PromotePieceComponent {

  constructor(private promotionService: PiecePromotionService, private modalSubjects: ModalSubjectsService) {
  }

  pieceColor: string = null;

  chosen(pieceCode) {
    this.promotionService.piecePromotionSubject.next(pieceCode);
    this.modalSubjects.closeModalSubject.next();
  }

  unicodePromotedPiece(piece: string) {
    return PieceToUnicodeTranslationUtil.pieceToUnicode(this.colorReverse(this.pieceColor) + piece);
  }

  colorReverse(color) {
    return color === 'W' ? 'B' : 'W';
  }

}
