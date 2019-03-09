package com.engwork.pgndb.userssettings;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSettingsDao {
  private UUID userId;
  private String key;
  private String value;
}
