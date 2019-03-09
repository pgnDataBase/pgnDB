package com.engwork.pgndb.tagssearching;

import lombok.Data;

@Data
class SearchTagsRequest {
  private String databaseName;
  private String value;
}
