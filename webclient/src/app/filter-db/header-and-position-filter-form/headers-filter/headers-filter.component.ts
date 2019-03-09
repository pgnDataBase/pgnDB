import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

export class SingleHeaderFilter {
  key: string = '';
  value: string = '';
}

@Component({
  selector: 'chessdb-headers-filter',
  templateUrl: './headers-filter.component.html',
  styleUrls: ['./headers-filter.component.scss']
})
export class HeadersFilterComponent implements OnInit {

  // variable that models headers filters
  headerFilters: SingleHeaderFilter[] = [];
  @Output()
  headerFiltersChanged: EventEmitter<SingleHeaderFilter[]> = new EventEmitter<SingleHeaderFilter[]>();
  @Input()
  initialFilters: SingleHeaderFilter[] = null;


  constructor() { }

  ngOnInit() {
    if (this.initialFilters == null) {
      this.headerFilters.push(new SingleHeaderFilter());
    } else {
      this.headerFilters = this.initialFilters;
    }
  }

  addMoreHeaderFiltersClicked() {
    let newFilter = new SingleHeaderFilter();
    this.headerFilters.push(newFilter);
  }

  deleteFilter(filter: SingleHeaderFilter) {
    let index = this.headerFilters.findIndex(f => f.key === filter.key);
    this.headerFilters.splice(index, 1);
  }

  filtersChanged() {
    this.headerFiltersChanged.next(this.headerFilters);
  }
}
