package com.engwork.pgndb.users.platformaccess;

import com.engwork.pgndb.users.requestmodel.SignUpRequest;

public interface PlatformAccessBean {

  void signUp(SignUpRequest signUpRequest);

  String signIn(String username, String password);

  void signOut(String token);

  String generateTokenForUser(String username);
}
