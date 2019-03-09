package com.engwork.pgndb.users.platformaccess;

import com.engwork.pgndb.exceptionhandling.ValidationException;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.users.User;
import com.engwork.pgndb.users.UsersBean;
import com.engwork.pgndb.users.requestmodel.RemoveAccountRequest;
import com.engwork.pgndb.users.requestmodel.SignInRequest;
import com.engwork.pgndb.users.requestmodel.SignUpRequest;
import com.engwork.pgndb.users.responsemodel.TokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
class PlatformAccessController {

  private final UsersBean usersBean;
  private final PlatformAccessBean platformAccessBean;

  private final RemoveAccountBean removeAccountBean;
  private final JWTValidator validator;

  @PostMapping("/user/sign-up")
  public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest) {
    if (usersBean.checkIfUsernameExists(signUpRequest.getUsername())) {
      throw ValidationException.forSingleObjectAndFieldAndReason("Username validation",
                                                                 "username",
                                                                 "Username already taken!");
    }

    platformAccessBean.signUp(signUpRequest);
    String token = platformAccessBean.signIn(signUpRequest.getUsername(), signUpRequest.getPassword());
    return ResponseEntity.ok().body(new TokenResponse(token));
  }

  @PostMapping("/user/sign-in")
  public ResponseEntity signIn(@RequestBody SignInRequest signInRequest) {
    String token = platformAccessBean.signIn(signInRequest.getUsername(), signInRequest.getPassword());
    if (token == null) {
      throw ValidationException.forSingleObjectAndFieldAndReason("Sign-in validation",
                                                                 "password/username",
                                                                 "Wrong password or username!");
    } else {
      return ResponseEntity.ok().body(new TokenResponse(token));
    }
  }

  @PostMapping("/user/sign-out")
  public ResponseEntity signOut(@RequestHeader(value = "Authorization") String tokenHeader) {
    String token = tokenHeader.replace("Bearer ", "");
    platformAccessBean.signOut(token);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/user/token")
  public ResponseEntity refreshToken(@RequestHeader(value = "Authorization") String tokenHeader) {
    String token = tokenHeader.replace("Bearer ", "");
    User user = validator.validate(token);
    String newToken = platformAccessBean.generateTokenForUser(user.getUsername());
    return ResponseEntity.ok().body(new TokenResponse(newToken));
  }

  @PostMapping("/user/remove-account")
  public ResponseEntity removeAccount(@RequestBody RemoveAccountRequest removeAccountRequest,
                                      @RequestHeader(value = "Authorization") String tokenHeader) {
    String token = tokenHeader.replace("Bearer ", "");
    User user = validator.validate(token);
    if (removeAccountBean.removeAccount(user.getUsername(), removeAccountRequest.getPassword())) {
      platformAccessBean.signOut(token);
      return ResponseEntity.ok(null);
    } else {
      throw ValidationException.forSingleObjectAndFieldAndReason("Password Validation",
                                                                 "password",
                                                                 "Wrong password!");
    }
  }
}
