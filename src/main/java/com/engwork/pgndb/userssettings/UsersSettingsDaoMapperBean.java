package com.engwork.pgndb.userssettings;

import java.util.List;
import java.util.Map;
import java.util.UUID;

interface UsersSettingsDaoMapperBean {

  List<UserSettingsDao> toDao(Map<String, String> configuration, UUID userId);

  Map<String, String> fromDao(List<UserSettingsDao> userSettingsDaos);
}
