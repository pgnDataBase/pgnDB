import { Component, OnInit } from '@angular/core';
import {Constants} from '../../model/Constants';

@Component({
  selector: 'chessdb-current-ip-displayer',
  templateUrl: './current-ip-displayer.component.html',
  styleUrls: ['./current-ip-displayer.component.scss']
})
export class CurrentIpDisplayerComponent implements OnInit {

  constructor() { }

  currentIp: string;

  ngOnInit() {
    this.currentIp = Constants.SERVER_IP;
  }

}
