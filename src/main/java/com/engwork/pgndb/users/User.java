package com.engwork.pgndb.users;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private UUID id;
  private String username;
  private byte[] password;
  private byte[] salt;
}
