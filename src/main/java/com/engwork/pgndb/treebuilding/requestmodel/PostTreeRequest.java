package com.engwork.pgndb.treebuilding.requestmodel;

import lombok.Data;

@Data
public class PostTreeRequest {
  private String databaseName;
  private Boolean includeVariants = false;
}
