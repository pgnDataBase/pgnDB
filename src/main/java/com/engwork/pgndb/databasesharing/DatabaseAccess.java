package com.engwork.pgndb.databasesharing;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseAccess {
  private UUID ownerId;
  private UUID databaseId;
  private UUID userId;
}
