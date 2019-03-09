package com.engwork.pgndb.tagssearching;

import com.engwork.pgndb.accessauthorization.AccessAuthorizationBean;
import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.users.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
class TagsSearchingController {

  private final TagsSearchingBean tagsSearchingBean;
  private final AccessAuthorizationBean accessAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;
  private final JWTValidator validator;

  @PostMapping(value = "/search/tags")
  public ResponseEntity getChessGames(@RequestBody SearchTagsRequest searchTagsRequest,
                                      @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), searchTagsRequest.getDatabaseName())) {
      throw accessExceptionsFactory.noAccessWithName(searchTagsRequest.getDatabaseName());
    }
    return ResponseEntity.ok(tagsSearchingBean.search(searchTagsRequest));
  }
}
