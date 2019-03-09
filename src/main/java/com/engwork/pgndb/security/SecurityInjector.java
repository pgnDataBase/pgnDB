package com.engwork.pgndb.security;

import com.engwork.pgndb.appconfig.securityconfig.SecurityConfig;
import com.engwork.pgndb.security.authentication.JWTAuthenticationEntryPoint;
import com.engwork.pgndb.security.authentication.JWTAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

import java.util.Collections;

@Configuration
@AllArgsConstructor
public class SecurityInjector {

  private final SecurityConfig securityConfig;

  @Bean
  public JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
    return new JWTAuthenticationEntryPoint();
  }

  @Bean
  public JWTAuthenticationProvider jwtAuthenticationProvider() {
    return new JWTAuthenticationProvider(jwtValidator());
  }

  @Bean
  public JWTGenerator jwtGenerator() {
    return new JWTGenerator(securityConfig);
  }

  @Bean
  public JWTValidator jwtValidator() {
    return new JWTValidator(securityConfig);
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider()));
  }
}
