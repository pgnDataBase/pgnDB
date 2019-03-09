package com.engwork.pgndb.security.authentication;

import com.engwork.pgndb.security.model.JWTAuthenticationToken;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JWTAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String TOKEN_START = "Bearer ";
  private static final String EMPTY_STRING = "";

  public JWTAuthenticationTokenFilter() {
    super("/api/**");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String requestAuthHeader = request.getHeader(AUTHORIZATION_HEADER);
    if (requestAuthHeader == null) {
      throw new AuthenticationCredentialsNotFoundException("JWT Token is missing!");
    }

    String extractedToken;
    if (requestAuthHeader.startsWith(TOKEN_START)) {
      extractedToken = requestAuthHeader.replace(TOKEN_START, EMPTY_STRING);
    } else {
      throw new AuthenticationCredentialsNotFoundException("JWT Token is invalid!");
    }
    JWTAuthenticationToken token = new JWTAuthenticationToken(extractedToken);
    return getAuthenticationManager().authenticate(token);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }
}
