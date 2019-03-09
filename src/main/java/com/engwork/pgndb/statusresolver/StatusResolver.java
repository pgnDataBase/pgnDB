package com.engwork.pgndb.statusresolver;

public interface StatusResolver {

  Status getStatusByKey(String key);

  void initStatus(String key, Double unit);

  void updateStatusByKey(String key, Double value);

  void addByUnitWithMultiplier(String key, Double multiplier);

  void removeByKey(String key);

}
