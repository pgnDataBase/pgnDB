package com.engwork.pgndb.statusresolver;

import com.engwork.pgndb.statusresolver.exceptions.StatusByKeyNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class LoadingStatusResolver implements StatusResolver {

  private final Map<String, Status> statuses = new ConcurrentHashMap<>();

  @Override
  public Status getStatusByKey(String key) {
    return this.statuses.getOrDefault(key, null);
  }

  @Override
  public void initStatus(String key, Double unit) {
    this.statuses.put(key, new Status(0.0, unit * 100));
  }

  @Override
  public void updateStatusByKey(String key, Double value) {
    if (this.statuses.containsKey(key)) {
      Status status = this.statuses.get(key);
      status.setValue(value);
    } else {
      throw new StatusByKeyNotFoundException(key);
    }
  }

  @Override
  public void addByUnitWithMultiplier(String key, Double multiplier) {
    if (this.statuses.containsKey(key)) {
      Status status = this.statuses.get(key);
      status.setValue(status.getUnit() * multiplier + status.getValue());
    } else {
      throw new StatusByKeyNotFoundException(key);
    }
  }

  @Override
  public void removeByKey(String key) {
    this.statuses.remove(key);
  }
}
