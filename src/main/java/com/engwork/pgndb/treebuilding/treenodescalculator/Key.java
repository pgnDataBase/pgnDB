package com.engwork.pgndb.treebuilding.treenodescalculator;

import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class Key {
  private final UUID startPositionId;
  private final String moveSan;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Key key = (Key) o;
    return Objects.equals(startPositionId, key.startPositionId) &&
        Objects.equals(moveSan, key.moveSan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startPositionId, moveSan);
  }
}
