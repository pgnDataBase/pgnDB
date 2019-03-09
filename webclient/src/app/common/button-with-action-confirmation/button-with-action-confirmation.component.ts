import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'chessdb-button-with-action-confirmation',
  templateUrl: './button-with-action-confirmation.component.html',
  styleUrls: ['./button-with-action-confirmation.component.scss']
})
export class ButtonWithActionConfirmationComponent implements OnInit {

  constructor() { }

  state: 'INIT' | 'CONFIRM' = 'INIT';
  @Output()
  actionClickedAndConfirmed: EventEmitter<void> = new EventEmitter();

  ngOnInit() {
  }

  clickedInitAction() {
    this.state = 'CONFIRM';
  }

  clickedConfirm() {
    this.actionClickedAndConfirmed.next();
    this.state = 'INIT';
  }

  clickedCancel() {
    this.state = 'INIT';
  }

}
