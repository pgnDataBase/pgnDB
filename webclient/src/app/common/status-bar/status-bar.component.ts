import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ModelStateService} from '../../../services/state-holders/model-state.service';
import {ServerService} from '../../../services/backend-communication/server.service';

@Component({
  selector: 'chessdb-status-bar',
  templateUrl: './status-bar.component.html',
  styleUrls: ['./status-bar.component.scss']
})
export class StatusBarComponent implements OnInit, OnDestroy {

  constructor(private server: ServerService,
              private model: ModelStateService) { }

  @Input()
  timeBetweenQueries: number = 4000;
  progressPercentage: number = 0;
  queryInterval = null;
  receivedResponseForLastQuery: boolean = true;

  @Input()
  STATUS_TYPE: 'FILE' | 'TREE';

  ngOnInit() {
    this.queryInterval = setInterval(() => {
      this.statusResolve().then(res => {
        this.progressPercentage = res;
        this.receivedResponseForLastQuery = true;
      })
    }, this.timeBetweenQueries);
  }

  ngOnDestroy() {
    if (this.queryInterval != null) {
      clearInterval(this.queryInterval);
    }
  }

  statusResolve(): Promise<any> {
    if (!this.receivedResponseForLastQuery) {
      return;
    }
    if (this.STATUS_TYPE === 'FILE') {
      return this.server.resolveUploadStatus(this.model.currentDB.name);
    }
    if (this.STATUS_TYPE === 'TREE') {
      return this.server.treeBuildingStatus(this.model.currentDB.name);
    }
  }

}
