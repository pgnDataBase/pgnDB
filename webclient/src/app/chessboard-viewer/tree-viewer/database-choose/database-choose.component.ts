import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ModelStateService} from '../../../../services/state-holders/model-state.service';
import {ChessDB} from '../../../model/ChessDB';

@Component({
  selector: 'chessdb-database-choose',
  templateUrl: './database-choose.component.html',
  styleUrls: ['./database-choose.component.scss']
})
export class DatabaseChooseComponent implements OnInit {

  currentDatabases: ChessDB[];
  @Output()
  databaseChosen: EventEmitter<ChessDB> = new EventEmitter<ChessDB>();
  @Output()
  cancelClicked: EventEmitter<void> = new EventEmitter<void>();

  constructor(private model: ModelStateService) {
    this.currentDatabases = model.currentDatabases;
  }

  ngOnInit() {
  }

}
