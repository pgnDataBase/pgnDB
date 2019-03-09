package com.engwork.pgndb.security;

import com.engwork.pgndb.appconfig.securityconfig.SecurityConfig;
import com.engwork.pgndb.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import lombok.Data;

@Data
public class JWTValidator {

  private final HashSet<String> tokensBlackList = new HashSet<>();
  private final SecurityConfig securityConfig;

  private static final String USER_ID = "userId";

  public JWTValidator(SecurityConfig securityConfig) {
    this.securityConfig = securityConfig;
  }

  public User validate(String token) {
    if (tokensBlackList.contains(token))
      return null;
    Claims body;
    try {
      body = Jwts.parser().setSigningKey(securityConfig.getJwtSecret()).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException exp) {
      return null;
    }
    User user = new User();
    user.setUsername(body.getSubject());
    user.setId(UUID.fromString((String) body.get(USER_ID)));
    return user;
  }

  public void deactivateToken(String token) {
    tokensBlackList.add(token);
    clearTokensBlackList();
  }

  private void clearTokensBlackList() {
    Iterator<String> blackListIterator = tokensBlackList.iterator();
    while (blackListIterator.hasNext()) {
      String blackListToken = blackListIterator.next();
      try {
        Jwts.parser().setSigningKey(securityConfig.getJwtSecret()).parse(blackListToken);
      } catch (ExpiredJwtException exp) {
        blackListIterator.remove();
      }
    }
  }
}
