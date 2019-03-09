package com.engwork.pgndb.userssettings;

import java.util.Map;
import java.util.UUID;

interface UsersSettingsBean {

  void createSettingsByUserId(Map<String, String> settings, UUID userId);

  Map<String, String> getSettingsByUserId(UUID userId);

}
