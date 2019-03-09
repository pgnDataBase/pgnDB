package com.engwork.pgndb.treebuilding.responsemodel;

import com.engwork.pgndb.treebuilding.treestatus.TreeStatus;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TreeNodesResponse {
  private List<TreeNodeResponse> treeNodes;
  private Integer total;
  private Boolean isTreeActual;
  private Boolean isTreeWithVariants;

  static public TreeNodesResponse create(List<TreeNodeResponse> treeNodeResponses, Integer total, TreeStatus treeStatus) {
    return new TreeNodesResponse(
        treeNodeResponses,
        total,
        treeStatus.getIsTreeUpToDate(),
        treeStatus.getIsTreeWithVariants()
    );
  }
}
