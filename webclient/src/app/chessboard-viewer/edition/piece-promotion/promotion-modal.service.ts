import { Injectable } from '@angular/core';
import {PromotePieceComponent} from './promote-piece/promote-piece.component';
import {ModalMessageService} from '../../../common/modal-message/modal-message.service';
import {ModalSubjectsService} from '../../../common/modal-message/modal-subjects.service';

@Injectable({
  providedIn: 'root'
})
export class PromotionModalService {

  constructor(private modal: ModalMessageService, private modalSubjects: ModalSubjectsService) { }

  openPromotionModal(pieceColor) {
    this.modalSubjects.openModalSubject.next({comp: PromotePieceComponent, data: {pieceColor: pieceColor}});
  }
}
