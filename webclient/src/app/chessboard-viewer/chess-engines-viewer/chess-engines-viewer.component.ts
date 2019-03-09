import {Component, EventEmitter, Input, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {ChessEnginesService} from './chess-engines.service';
import {Engine, EngineResult} from './engines-model';
import {Move} from '../../model/Move';

@Component({
  selector: 'chessdb-chess-engines-viewer',
  templateUrl: './chess-engines-viewer.component.html',
  styleUrls: ['./chess-engines-viewer.component.scss']
})
export class ChessEnginesViewerComponent implements OnInit, OnDestroy {

  constructor(private chessEnginesService: ChessEnginesService) { }

  isLoading: boolean = false;
  availableEngines: Engine[];
  isFail: boolean = false;
  failMessage: string = null;
  isQueryingEngine: boolean = false;
  chosenEngine: Engine = null;
  currentEngineResults: EngineResult[] = [];
  queryInterval = null;
  timeBetweenEvaluations: number = 6000;
  timesQueriedResult: number = 0;
  queryingFor: Move = null;
  @Input()
  currentMove: Move = null;


  @Output()
  closeButtonClicked: EventEmitter<void> = new EventEmitter();

  ngOnInit() {
    this.getAvailableEngines();
  }

  async getAvailableEngines() {
    this.isLoading = true;
    await this.chessEnginesService.getEnginesList().then(res => {
      this.availableEngines = res.info;
      if (this.availableEngines.length > 0) {
        this.chosenEngine = this.availableEngines[0];
      } else {
        this.isFail = true;
        this.failMessage = 'No engines detected';
      }
    }).catch(err => {
      this.isFail = true;
      this.failMessage = 'Chess engines module is not available.' +
        ' If you did try to start it up make sure that configuration of this module is correct';
    });
    this.isLoading = false;
  }

  ngOnDestroy() {
    if (this.queryInterval) {
      clearInterval(this.queryInterval);
    }
    if (this.isQueryingEngine) {
      this.chessEnginesService.stopEngine();
    }
  }

  startQueryingEngine() {
    this.isQueryingEngine = true;
    this.queryingFor = this.currentMove;
    this.currentEngineResults = [];
    this.timesQueriedResult = 0;
    if (this.chosenEngine != null) {
      this.chessEnginesService
        .startEngine({
          fen: this.queryingFor.fen,
          engine: this.chosenEngine.name,
          seconds: 60})
        .then(res => {
          this.queryInterval = setInterval(() => {
            this.evaluateEngineResult();
            this.timesQueriedResult++;
            if (this.timesQueriedResult >= 10) {
              clearInterval(this.queryInterval);
            }
          }, this.timeBetweenEvaluations);
        })
        .catch((err) => {
          this.isFail = true;
          this.failMessage = 'Server error when trying to start engine';
          this.isQueryingEngine = false;
        });
    }
  }

  evaluateEngineResult() { console.log('evaluating');
    this.chessEnginesService.getEngineResult().then(res => {
      this.currentEngineResults = res;
    }).catch(err => {
      clearInterval(this.queryInterval);
      this.isQueryingEngine = false;
      this.isFail = true;
      this.failMessage = 'Server error trying to get engine result';
    });
  }

  stopQueryingEngine() {
    this.isLoading = true;
    clearInterval(this.queryInterval);
    this.chessEnginesService.stopEngine().then(() => {
      this.isLoading = false;
      this.isQueryingEngine = false;
    }).catch(() => {
      this.isLoading = false;
      this.isQueryingEngine = false;
    });
  }

}
