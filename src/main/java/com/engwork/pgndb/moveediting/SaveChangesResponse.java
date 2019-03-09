package com.engwork.pgndb.moveediting;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class SaveChangesResponse {
  private UUID gameId;
}
