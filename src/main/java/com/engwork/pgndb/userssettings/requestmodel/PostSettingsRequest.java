package com.engwork.pgndb.userssettings.requestmodel;

import java.util.Map;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSettingsRequest {
  private Map<String, String> settings;
}
