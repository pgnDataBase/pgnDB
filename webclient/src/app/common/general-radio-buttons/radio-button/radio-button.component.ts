import {Component, EventEmitter, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';

@Component({
  selector: 'chessdb-radio-button',
  templateUrl: './radio-button.component.html',
  styleUrls: ['./radio-button.component.scss']
})
export class RadioButtonComponent implements OnChanges {

  constructor() {
  }

  @Input()
  initiallyActive: boolean = false;
  @Input()
  currentlyActive: boolean = false;
  chosen: boolean = false;
  clickedOnButton: EventEmitter<RadioButtonComponent> = new EventEmitter<RadioButtonComponent>();
  currentlyActiveButtonChangedByInput: EventEmitter<RadioButtonComponent> = new EventEmitter<RadioButtonComponent>();

  ngOnChanges(changes: SimpleChanges) {
    if (changes.currentlyActive && changes.currentlyActive.currentValue === true) {
      this.currentlyActiveButtonChangedByInput.next(this);
    }
  }

}
