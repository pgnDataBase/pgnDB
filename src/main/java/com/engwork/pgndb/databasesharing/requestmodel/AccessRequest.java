package com.engwork.pgndb.databasesharing.requestmodel;

import java.util.UUID;
import lombok.Data;

@Data
public class
AccessRequest {
  private UUID databaseId;
  private String username;
}
