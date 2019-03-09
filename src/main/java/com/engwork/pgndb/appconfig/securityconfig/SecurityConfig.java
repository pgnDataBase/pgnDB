package com.engwork.pgndb.appconfig.securityconfig;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConfig {

  private String encryptionSecret;
  private String jwtSecret;
  private int jwtExpirationMinutes;
  private List<String> cors;

  private final static String ENCRYPTION_SECRET = "encryptionSecret";
  private final static String JWT_SECRET = "jwtSecret";
  private final static String JWT_EXPIRATION_MINUTES = "jwtExpirationMinutes";
  private final static String CORS = "cors";

  static SecurityConfig fromJson(JSONObject jsonObject) {
    try {
      SecurityConfigBuilder securityConfigBuilder = new SecurityConfigBuilder();
      securityConfigBuilder.encryptionSecret = jsonObject.getString(ENCRYPTION_SECRET);
      securityConfigBuilder.jwtSecret = jsonObject.getString(JWT_SECRET);
      securityConfigBuilder.jwtExpirationMinutes = jsonObject.getInt(JWT_EXPIRATION_MINUTES);
      securityConfigBuilder.cors = readCors(jsonObject);
      return securityConfigBuilder.build();
    } catch (Exception exception) {
      log.error("Invalid security configuration!");
      throw new SecurityConfigException(exception);
    }

  }

  private static List<String> readCors(JSONObject jsonObject) {
    List<String> cors = new ArrayList<>();
    JSONArray jsonArray = jsonObject.getJSONArray(CORS);
    int length = jsonArray.length();
    for (int index = 0; index < length; index++) {
      cors.add(jsonArray.get(index).toString());
    }
    return cors;
  }
}
