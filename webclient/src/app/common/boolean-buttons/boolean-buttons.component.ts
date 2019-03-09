import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'chessdb-boolean-buttons',
  templateUrl: './boolean-buttons.component.html'
})
export class BooleanButtonsComponent implements OnInit {

  constructor() { }

  @Input()
  initialValue: boolean;

  ngOnInit() {
  }

  @Output()
  booleanClicked: EventEmitter<boolean> = new EventEmitter<boolean>();
}
