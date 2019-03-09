package com.engwork.pgndb.security.authentication;

import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.security.model.JWTAuthenticationToken;
import com.engwork.pgndb.security.model.JWTUserDetails;
import com.engwork.pgndb.users.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class JWTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
  private final JWTValidator validator;

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

  }

  // Get user's data from token. If token is invalid throw AuthenticationException
  @Override
  protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    JWTAuthenticationToken JWTToken = (JWTAuthenticationToken) authentication;
    String token = JWTToken.getToken();
    // Extract user data from token
    User user = validator.validate(token);
    if (user == null) {
      throw new AuthenticationCredentialsNotFoundException("JWT Token is invalid!");
    }
    return new JWTUserDetails(user.getUsername());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (JWTAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
