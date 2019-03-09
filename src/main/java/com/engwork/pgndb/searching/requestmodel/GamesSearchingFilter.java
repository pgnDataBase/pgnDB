package com.engwork.pgndb.searching.requestmodel;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class GamesSearchingFilter {
  private HeaderFilter headerFilter;
  private PositionsFilter positionsFilter;
}
