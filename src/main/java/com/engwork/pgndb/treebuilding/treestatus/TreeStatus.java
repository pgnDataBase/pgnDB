package com.engwork.pgndb.treebuilding.treestatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TreeStatus {
  private Boolean isTreeUpToDate;
  private Boolean isTreeWithVariants;
}
