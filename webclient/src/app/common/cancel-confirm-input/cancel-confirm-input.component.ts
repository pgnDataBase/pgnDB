import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'chessdb-cancel-confirm-input',
  templateUrl: './cancel-confirm-input.component.html',
  styleUrls: ['./cancel-confirm-input.component.scss']
})
export class CancelConfirmInputComponent {

  constructor() { }

  @Output()
  clickedCancelInput: EventEmitter<void> = new EventEmitter();
  @Output()
  clickedConfirmInput: EventEmitter<string> = new EventEmitter();
  @Input()
  placeholder: string = '';
  @Input()
  required: boolean = true;
  @Input()
  inputWidth: number;
  @Input()
  type: string;
  @Input()
  maxLength: number;

  value: string = '';

  clickedCancel() {
    this.clickedCancelInput.next();
  }

  clickedConfirm() {
    this.clickedConfirmInput.next(this.value);
  }

}
