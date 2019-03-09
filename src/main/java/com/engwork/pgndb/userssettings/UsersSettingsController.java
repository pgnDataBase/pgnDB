package com.engwork.pgndb.userssettings;

import com.engwork.pgndb.exceptionhandling.ValidationException;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.users.User;
import com.engwork.pgndb.userssettings.requestmodel.PostSettingsRequest;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/settings")
@AllArgsConstructor
class UsersSettingsController {

  private final UsersSettingsBean usersSettingsBean;
  private final JWTValidator validator;

  @PostMapping("")
  public ResponseEntity saveSettings(@RequestBody PostSettingsRequest postSettingsRequest,
                                     @RequestHeader("Authorization") String token) {
    if (postSettingsRequest.getSettings() == null || postSettingsRequest.getSettings().isEmpty()) {
      throw ValidationException.forSingleObjectAndFieldAndReason("Post Configuration Request",
                                                                 "settings", "Cannot be null or empty");
    }
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    this.usersSettingsBean.createSettingsByUserId(postSettingsRequest.getSettings(), user.getId());
    return ResponseEntity.ok(null);
  }

  @GetMapping("")
  public ResponseEntity getSettingsForUser(@RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    Map<String, String> result = this.usersSettingsBean.getSettingsByUserId(user.getId());
    return ResponseEntity.ok(result);
  }

}
