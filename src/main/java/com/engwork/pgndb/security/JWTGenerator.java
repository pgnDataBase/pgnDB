package com.engwork.pgndb.security;

import com.engwork.pgndb.appconfig.securityconfig.SecurityConfig;
import com.engwork.pgndb.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Calendar;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JWTGenerator {

  private final SecurityConfig securityConfig;

  private static final String USER_ID = "userId";

  public String generate(User user) {
    Claims claims = Jwts
        .claims()
        .setSubject(user.getUsername());

    claims.put(USER_ID, user.getId().toString());

    Calendar expirationTime = Calendar.getInstance();
    expirationTime.add(Calendar.MINUTE, securityConfig.getJwtExpirationMinutes());

    return Jwts
        .builder()
        .setClaims(claims)
        .signWith(SignatureAlgorithm.HS512, securityConfig.getJwtSecret())
        .setExpiration(expirationTime.getTime())
        .compact();
  }
}
