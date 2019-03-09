import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'chessdb-round-button',
  templateUrl: './round-button.component.html',
  styleUrls: ['./round-button.component.scss']
})
export class RoundButtonComponent implements OnInit {

  @Output() click: EventEmitter<void> = new EventEmitter<void>();
  @Input() pointsToLeft: boolean = false;

  constructor() { }

  ngOnInit() {
  }

  clickedOnButton() {
    this.click.next();
  }

}
