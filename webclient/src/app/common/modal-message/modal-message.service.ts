import { Injectable } from '@angular/core';
import {ModalMessageComponent} from './modal-message.component';
import {ChessDBException} from '../../model/ChessDBException';
import {ModalSubjectsService} from './modal-subjects.service';
import {AuthenticationService} from '../../authorization/authentication.service';


@Injectable({
  providedIn: 'root'
})
export class ModalMessageService {

  constructor(private modalSubjects: ModalSubjectsService, private auth: AuthenticationService) { }

  openModalMessage(messageTitle?: string, messageContent?: string, closeButtonMessage?: string) {
    this.modalSubjects.openModalSubject.next(
      {comp: ModalMessageComponent,
        data: {messageTitle: messageTitle, messageContent: messageContent, closeButtonMessage: closeButtonMessage}});
  }

  closeModalMessage() {
    this.modalSubjects.openModalSubject.next(null);
  }

  getModalSubjects() {
    return this.modalSubjects;
  }

  openModalMessageOnChessDBException(chessDBException) {
    if (chessDBException.error == 'Unauthorized') {
      this.openModalMessage('Unauthorized', 'You will be logged out');
      this.auth.logoutWithoutHttp();
      return;
    }
    let toDisplay: ChessDBException = new ChessDBException();
    if (typeof chessDBException == 'string') {
      toDisplay.headerMessage = 'Server responded with error in string format (which it should not)';
      toDisplay.mainMessage = chessDBException;
      this.openModalMessage(toDisplay.headerMessage, toDisplay.mainMessage);
      return;
    }
    if (chessDBException == null ||
      chessDBException.headerMessage == null) {
      toDisplay.headerMessage = 'Encountered unexpected error.';
    } else {
      toDisplay.headerMessage = chessDBException.headerMessage;
    }
    if (chessDBException == null ||
      chessDBException.mainMessage == null) {
      toDisplay.mainMessage = 'No specific information was specified about this error.';
    } else {
      toDisplay.mainMessage = chessDBException.mainMessage;
    }
    this.openModalMessage(toDisplay.headerMessage, toDisplay.mainMessage);
  }
}
