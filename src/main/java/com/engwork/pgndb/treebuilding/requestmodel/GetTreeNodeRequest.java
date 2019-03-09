package com.engwork.pgndb.treebuilding.requestmodel;

import java.util.UUID;
import lombok.Data;

@Data
public class GetTreeNodeRequest {
  private String databaseName;
  private Integer limit;
  private Integer offset;
  private UUID positionId;
  private String fen;
}