import {Component, OnInit} from '@angular/core';
import {DbFilterService} from '../../../services/db-filter.service';
import {ModelStateService} from '../../../services/state-holders/model-state.service';
import {HeadersAndPositionFilter} from '../../model/HeadersAndPositionFilter';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';
import {Router} from '@angular/router';
import {ROUTING_PATHS} from '../../../routing/routing-paths';
import {ServerService} from '../../../services/backend-communication/server.service';
import {SingleHeaderFilter} from './headers-filter/headers-filter.component';
import {PositionFilters} from './positions-filter/positions-filter.component';
import {FilterConversionUtil} from './filter-conversion-util';


@Component({
  selector: 'chessdb-header-filter-form',
  templateUrl: './header-and-position-filter-form.component.html',
  styleUrls: ['./header-and-position-filter-form.component.scss']
})
export class HeaderAndPositionFilterFormComponent implements OnInit {
  constructor(private dbFilter: DbFilterService,
              private model: ModelStateService,
              private router: Router,
              private server: ServerService,
              private modalMessage: ModalMessageService) {
  }

  ngOnInit() {
    if (this.model.currentDB != null) {
      let alreadyExistingFilter =
        this.dbFilter.getHeaderAndPositionFilterForDb(this.model.currentDB.name);
      if (alreadyExistingFilter != null) {
        this.positionFilters = alreadyExistingFilter.positionsFilter;
        this.headerFilters =
          FilterConversionUtil.chessGameMetadataToSingleHeaderFilterArray(alreadyExistingFilter.headerFilter);
      }
      this.POSITIONS_FILTER_ACTIVE = this.positionFilters != null
        && this.positionFilters.positions != null;
      this.HEADERS_FILTER_ACTIVE = this.headerFilters != null && this.headerFilters.length > 0;
    }
  }

  displayMode: 'HEADERS' | 'POSITIONS' = 'HEADERS';
  filterMode: 'ALL' | 'CURRENT' = 'CURRENT';

  // output from headers filter component
  headerFilters: SingleHeaderFilter[] = [];
  // output from positions filter component
  positionFilters: PositionFilters;

  HEADERS_FILTER_ACTIVE: boolean = false;
  POSITIONS_FILTER_ACTIVE: boolean = false;

  getDbNamesForFilter() {
    if (this.filterMode === 'CURRENT') {
      return this.model.currentDB == null ? null : [this.model.currentDB.name];
    }
    if (this.filterMode === 'ALL') {
      return this.model.currentDatabases.map(db => db.name);
    }
  }

  isHeadersFilterActive() {
    for (let filter of this.headerFilters) {
      if (filter.key.length > 0 && filter.value.length > 0) {
        return true;
      }
    }
    return false;
  }

  isPositionFilterActive() {
    if (!(this.positionFilters != null
      && this.positionFilters.positions != null &&
      this.positionFilters.positions.length > 0)) {
      return false;
    }
    for (let position of this.positionFilters.positions) {
      if (position.length == 0) {
        return false;
      }
    }
    return true;
  }

  getHeadersAndPositionsFilter(): HeadersAndPositionFilter {
    if (!this.isHeadersFilterActive() && !this.isPositionFilterActive()) {
      return null;
    }
    let filter = new HeadersAndPositionFilter();
    if (this.isHeadersFilterActive()) {
      filter.headerFilter = FilterConversionUtil.singleHeaderFilterArrayToChessGameMetadata(this.headerFilters);
    }
    if (this.isPositionFilterActive()) {
      filter.positionsFilter = {includeVariants: false, positions: []};
      filter.positionsFilter.positions = this.positionFilters.positions;
      filter.positionsFilter.includeVariants = this.positionFilters.includeVariants;
    }
    return filter;
  }

  applyFilterClicked() {
    const dbNamesToSet: any[] = this.getDbNamesForFilter();
    if (dbNamesToSet == null) {
      this.modalMessage.openModalMessage('Choose database first!',
        'Database has to be chosen before setting filter.');
      return;
    }
    const filter = this.getHeadersAndPositionsFilter();
    dbNamesToSet.forEach(dbName => {
      this.dbFilter.setHeaderAndPositionFilterForDb(dbName, filter);
    });
    if (this.filterMode === 'CURRENT') {
      this.model.setAndEmitCurrentGames(null);
      this.router.navigate([ROUTING_PATHS.GAMES_LIST]).then(() => {
        this.server.downloadAndRefreshGames(this.model.currentDB);
      });
    }
  }

  deleteFilterClicked() {
    const dbNamesToSet: any[] = this.getDbNamesForFilter();
    if (dbNamesToSet == null) {
      this.modalMessage.openModalMessage('Choose database first!',
        'Database has to be chosen before setting filter.');
      return;
    }
    dbNamesToSet.forEach(dbName => {
      this.dbFilter.setHeaderAndPositionFilterForDb(dbName, null);
    });
    if (this.filterMode === 'CURRENT') {
      this.model.setAndEmitCurrentGames(null);
      this.router.navigate([ROUTING_PATHS.GAMES_LIST]).then(() => {
        this.server.downloadAndRefreshGames(this.model.currentDB);
      });
    }
  }

  onHeadersFilterChanged($event) {
    this.headerFilters = $event;
    this.HEADERS_FILTER_ACTIVE = this.isHeadersFilterActive();
  }

  onPositionsFilterChanged($event) {
    this.positionFilters = $event;
    this.POSITIONS_FILTER_ACTIVE = this.isPositionFilterActive();
  }
}
