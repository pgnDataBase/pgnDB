package com.engwork.pgndb.databasemanager;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@AllArgsConstructor
class DynamicDataSource extends AbstractRoutingDataSource {

  private DatabaseContainer databaseContainer;

  @Override
  protected Object determineCurrentLookupKey() {
    return this.databaseContainer.getActiveDatabase();
  }
}
