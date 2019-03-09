import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'chessdb-plain-row-display',
  templateUrl: './plain-row-display.component.html',
  styleUrls: ['./plain-row-display.component.scss']
})
export class PlainRowDisplayComponent implements OnInit {

  @Input()
  values: string[] = [];
  @Input()
  clickable: boolean = false;
  @Input()
  bold: boolean = false;

  constructor() { }

  ngOnInit() {
  }

}
