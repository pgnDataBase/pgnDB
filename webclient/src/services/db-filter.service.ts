import { Injectable } from '@angular/core';
import {HeadersAndPositionFilter} from '../app/model/HeadersAndPositionFilter';
import {Subject} from 'rxjs';

export class FilteringChange {
  dbName: string;
  filtered: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class DbFilterService {

  constructor() { }

  filteringChanged: Subject<FilteringChange> = new Subject<FilteringChange>();

  public headersAndPositionFilterForDbs = {};

  getHeaderAndPositionFilterForDb(dbName: string): HeadersAndPositionFilter {
    return this.headersAndPositionFilterForDbs[dbName];
  }

  setHeaderAndPositionFilterForDb(dbName: string, filter: HeadersAndPositionFilter) {
    this.headersAndPositionFilterForDbs[dbName] = filter;
    this.filteringChanged.next({dbName: dbName, filtered: filter != null});
  }
}
