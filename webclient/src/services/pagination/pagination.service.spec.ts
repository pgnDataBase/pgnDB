import { TestBed, inject } from '@angular/core/testing';

import {PaginationService, Bounds, PaginationContext} from './pagination.service';

describe
('PaginationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PaginationService]
    });
  });

  it('should be created', inject([PaginationService], (service: PaginationService) => {
    expect(service).toBeTruthy();
  }));

  it('should return proper bounds', inject([PaginationService], (service: PaginationService) => {
    let bounds: Bounds = service.itemsThatAreOnGivenPage(0, 10);
    expect(bounds.lowerBound).toBe(0);
    expect(bounds.upperBound).toBe(9);

    bounds = service.itemsThatAreOnGivenPage(3, 10);
    expect(bounds.lowerBound).toBe(30);
    expect(bounds.upperBound).toBe(39);

    bounds = service.itemsThatAreOnGivenPage(5, 3);
    expect(bounds.lowerBound).toBe(15);
    expect(bounds.upperBound).toBe(17);

    bounds = service.itemsThatAreOnGivenPage(1, 3);
    expect(bounds.lowerBound).toBe(3);
    expect(bounds.upperBound).toBe(5);
  }));

  it('should return proper max page', inject([PaginationService], (service: PaginationService) => {
    let context: PaginationContext =
      {itemsTotal: 15, pageSize: 6, contextName: null, currentPage: null };
    let maxPage = service.maxPageForContextObject(context);
    expect(maxPage).toBe(2);

    context.pageSize = 5;
    context.itemsTotal = 16;
    maxPage = service.maxPageForContextObject(context);
    expect(maxPage).toBe(3);

    context.pageSize = 5;
    context.itemsTotal = 15;
    maxPage = service.maxPageForContextObject(context);
    expect(maxPage).toBe(2);

    context.pageSize = 10;
    context.itemsTotal = 4;
    maxPage = service.maxPageForContextObject(context);
    expect(maxPage).toBe(0);

    context.pageSize = 5;
    context.itemsTotal = 10;
    maxPage = service.maxPageForContextObject(context);
    expect(maxPage).toBe(1);

    context.pageSize = 5;
    context.itemsTotal = 11;
    maxPage = service.maxPageForContextObject(context);
    expect(maxPage).toBe(2);

    context.pageSize = 5;
    context.itemsTotal = 12;
    maxPage = service.maxPageForContextObject(context);
    expect(maxPage).toBe(2);
  }));
});
