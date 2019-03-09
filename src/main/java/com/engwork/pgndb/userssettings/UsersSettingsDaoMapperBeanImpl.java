package com.engwork.pgndb.userssettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class UsersSettingsDaoMapperBeanImpl implements UsersSettingsDaoMapperBean {

  public List<UserSettingsDao> toDao(Map<String, String> settings, UUID userId) {
    List<UserSettingsDao> result = new ArrayList<>();
    for (String key : settings.keySet()) {
      result.add(new UserSettingsDao(userId, key, settings.get(key)));
    }
    return result;
  }

  public Map<String, String> fromDao(List<UserSettingsDao> userSettingsDaos) {
    Map<String, String> result = new HashMap<>();
    userSettingsDaos.forEach(item -> {
      result.put(item.getKey(), item.getValue());
    });
    return result;
  }
}
