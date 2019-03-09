package com.engwork.pgndb.users.requestmodel;

import lombok.Data;

@Data
public class SignInRequest {
  private String username;
  private String password;
}
