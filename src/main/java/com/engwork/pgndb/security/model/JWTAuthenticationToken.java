package com.engwork.pgndb.security.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@EqualsAndHashCode(callSuper = true)
public class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {
  private String token;

  public JWTAuthenticationToken(String token) {
    super(null, null);
    this.token = token;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }
}
