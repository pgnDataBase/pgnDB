import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'chessdb-about-screen',
  templateUrl: './about-screen.component.html',
  styleUrls: ['./about-screen.component.scss']
})
export class AboutScreenComponent implements OnInit {

  constructor() { }

  @Output()
  aboutScreenClose: EventEmitter<void> = new EventEmitter<void>();

  ngOnInit() {
  }

}
