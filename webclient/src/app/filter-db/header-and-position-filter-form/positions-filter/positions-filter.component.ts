import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Constants} from '../../../model/Constants';
import {LibloaderService} from '../../../../services/library-related/libloader.service';
import {ChessboardService} from '../../../../services/library-related/chessboard.service';

declare var ChessBoard: any;

export class PositionFilters {
  positions: string[];
  includeVariants: boolean;
}

@Component({
  selector: 'chessdb-positions-filter',
  templateUrl: './positions-filter.component.html',
  styleUrls: ['./positions-filter.component.scss']
})
export class PositionsFilterComponent implements OnInit, OnDestroy {

  constructor(private libloader: LibloaderService,
              private chessboardService: ChessboardService) { }

  boardDisplay: any = null;

  // variable that models current filters
  fenFilters: string[] = [];
  // variable that models checkbox state
  includeVariantsPosition: boolean = false;

  @Output()
  positionFiltersChanged: EventEmitter<PositionFilters> = new EventEmitter();
  @Input()
  initialFilterState: PositionFilters = null;
  onMove: 'WHITE' | 'BLACK' | 'IGNORE' = 'IGNORE';

  loadBoard() {
    this.libloader.loadChessBoardJs();
    let checkExist = setInterval(() => {
      if (typeof ChessBoard !== 'undefined') {
        // js is already loaded
        clearInterval(checkExist);
        this.boardDisplay = ChessBoard('board', this.chessboardService.chessboardConfigTrash);
      }
    }, 100);
  }

  ngOnInit() {
    this.loadBoard();
    if (this.initialFilterState != null) {
      this.fenFilters = this.initialFilterState.positions;
      this.includeVariantsPosition = this.initialFilterState.includeVariants;
    } else {
      this.fenFilters.push('');
    }
  }

  ngOnDestroy(): void {
    this.libloader.deloadChessBoardJs();
  }

  flipBoard() {
    this.boardDisplay.flip();
  }

  addPositionFilterFromBoard() {
    let resultingFen = this.boardDisplay.position('fen');
    if (this.onMove === 'WHITE') {
      resultingFen += ' w';
    }
    if (this.onMove === 'BLACK') {
      resultingFen += ' b';
    }
    if (this.fenFilters.length == 1 && this.fenFilters[0] == '') {
      this.fenFilters[0] = resultingFen;
    } else {
      this.fenFilters.push(resultingFen);
    }
    this.propagateFilterChange();
    this.boardDisplay.position(Constants.START_FEN);
  }

  includeVariantsChanged($event) {
    this.includeVariantsPosition = $event.target.checked;
    this.propagateFilterChange();
  }

  propagateFilterChange() {
    this.positionFiltersChanged.next(this.computeCurrentFilter());
  }

  computeCurrentFilter() {
    return {
      positions: this.fenFilters,
      includeVariants: this.includeVariantsPosition
    }
  }

  deleteFenByIndex(index) {
    this.fenFilters.splice(index, 1);
    this.propagateFilterChange();
  }

  addMoreManuallyClicked() {
    this.fenFilters.push('');
    this.propagateFilterChange();
  }

  fenChangedFromInput($event, index) {
    let newFen = $event.target.value;
    this.fenFilters[index] = newFen;
    this.propagateFilterChange();
  }
}
