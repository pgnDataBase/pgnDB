package com.engwork.pgndb.security.configuration;

import com.engwork.pgndb.appconfig.securityconfig.SecurityConfig;
import com.engwork.pgndb.security.JWTSuccessHandler;
import com.engwork.pgndb.security.authentication.JWTAuthenticationEntryPoint;
import com.engwork.pgndb.security.authentication.JWTAuthenticationTokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {

  private JWTAuthenticationEntryPoint entryPoint;
  private AuthenticationManager authenticationManager;
  private SecurityConfig securityConfig;

  private static final String SIGN_UP_URL = "/api/user/sign-up";
  private static final String SIGN_IN_URL = "/api/user/sign-in";
  private static final String USERNAME_AVAILABILITY = "/api/username/availability";

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(SIGN_UP_URL);
    web.ignoring().antMatchers(SIGN_IN_URL);
    web.ignoring().antMatchers(USERNAME_AVAILABILITY);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors()
        .and()
        .authorizeRequests().antMatchers("/api/**").authenticated()
        .and()
        .exceptionHandling().authenticationEntryPoint(entryPoint)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    http
        .headers()
        .cacheControl();
  }

  private JWTAuthenticationTokenFilter authenticationTokenFilter() {
    JWTAuthenticationTokenFilter filter = new JWTAuthenticationTokenFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationSuccessHandler(new JWTSuccessHandler());
    return filter;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(securityConfig.getCors());
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
