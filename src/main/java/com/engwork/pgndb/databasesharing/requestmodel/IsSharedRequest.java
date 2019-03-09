package com.engwork.pgndb.databasesharing.requestmodel;

import java.util.UUID;
import lombok.Data;

@Data
public class IsSharedRequest {
  private UUID databaseId;
}
