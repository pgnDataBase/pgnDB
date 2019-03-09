package com.engwork.pgndb.searching.requestmodel;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PositionsFilter {
  private List<String> positions;
  private Boolean includeVariants = false;
}
