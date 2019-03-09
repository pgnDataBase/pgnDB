package com.engwork.pgndb.databasemerging;

import lombok.Data;

@Data
class MergeDatabasesRequest {
  private String from;
  private String to;
}
