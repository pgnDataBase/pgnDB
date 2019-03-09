import {Component, Input, OnInit} from '@angular/core';

enum InfoMessageType {
  INFO = "info",
  WARNING = "warning",
  ERROR = "error"
}

@Component({
  selector: 'chessdb-info-message',
  templateUrl: './info-message.component.html',
  styleUrls: ['./info-message.component.scss']
})
export class InfoMessageComponent implements OnInit {

  @Input() messageType: InfoMessageType = InfoMessageType.INFO;
  @Input() fontSize: string = '24px';

  constructor() { }

  ngOnInit() {
  }

}
