package com.engwork.pgndb.users;

import com.engwork.pgndb.users.requestmodel.SearchUsersRequest;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
class UsersController {

  private UsersBean usersBean;

  @PostMapping("/search/users")
  public ResponseEntity searchUsers(@RequestBody SearchUsersRequest searchUsersRequest) {
    return ResponseEntity.ok().body(usersBean.getUsersRegex(searchUsersRequest.getValue()));
  }

  @PostMapping("/username/availability")
  public ResponseEntity checkIfUsernameExists(@RequestBody String username) {
    username = new JSONObject(username).getString("username");
    return ResponseEntity.ok().body(!usersBean.checkIfUsernameExists(username));
  }
}
