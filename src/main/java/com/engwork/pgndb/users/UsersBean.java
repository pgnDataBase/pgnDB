package com.engwork.pgndb.users;

import java.util.List;
import java.util.UUID;

public interface UsersBean {

  UserMetadata getUserMetadataByUsername(String username);

  UserMetadata getUserMetadataByUserId(UUID userId);

  boolean checkIfUsernameExists(String username);

  boolean checkIfUserExists(UUID id);

  List<String> getUsersRegex(String value);
}
