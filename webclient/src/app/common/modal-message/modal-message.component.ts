import {Component, OnInit} from '@angular/core';
import {ModalSubjectsService} from './modal-subjects.service';

@Component({
  selector: 'chessdb-modal-message',
  templateUrl: './modal-message.component.html',
  styleUrls: ['./modal-message.component.scss']
})
export class ModalMessageComponent implements OnInit {

  constructor(private modalSubjects: ModalSubjectsService) {
  }

  messageTitle: string = null;
  messageContent: string = null;
  closeButtonMessage: string = 'Close';

  ngOnInit() {
  }

  closeModal() {
    this.modalSubjects.closeModalSubject.next();
  }

}
