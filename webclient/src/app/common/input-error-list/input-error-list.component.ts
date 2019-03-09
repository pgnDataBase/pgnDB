import {Component, Input, OnChanges, OnInit} from '@angular/core';

@Component({
  selector: 'chessdb-input-error-list',
  templateUrl: './input-error-list.component.html',
  styleUrls: ['./input-error-list.component.scss']
})
export class InputErrorListComponent implements OnInit, OnChanges {

  @Input() fontSize: string = '14px';
  @Input() errors : any;
  errorCodes = [];

  constructor() { }

  ngOnInit() {
    this.prepareKeys();
  }

  ngOnChanges() {
    this.prepareKeys();
  }

  prepareKeys() {
    if (!this.errors) return;
    let keys = [];
    Object.keys(this.errors).forEach(function(key) {
      keys.push(key);
    });
    this.errorCodes = keys;
  }

}
