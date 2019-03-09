package com.engwork.pgndb.databasemerging;

import com.engwork.pgndb.accessauthorization.AccessAuthorizationBean;
import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.users.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
class DatabaseMergingController {

  private final DatabaseMergingBean databaseMergingBean;
  private final AccessAuthorizationBean accessAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;
  private final JWTValidator validator;

  @PostMapping(value = "/chessdbs/merging")
  public ResponseEntity mergeDatabases(@RequestBody MergeDatabasesRequest mergeDatabasesRequest,
                                       @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), mergeDatabasesRequest.getFrom())) {
      throw accessExceptionsFactory.noAccessWithName(mergeDatabasesRequest.getFrom());
    }
    if (!accessAuthorizationBean.validateAccess(user.getId(), mergeDatabasesRequest.getTo())) {
      throw accessExceptionsFactory.noAccessWithName(mergeDatabasesRequest.getTo());
    }

    log.info("Started databases merging from {} to {}.", mergeDatabasesRequest.getFrom(), mergeDatabasesRequest.getTo());
    databaseMergingBean.merge(mergeDatabasesRequest.getFrom(), mergeDatabasesRequest.getTo());
    log.info("Success databases merging from {} to {}.", mergeDatabasesRequest.getFrom(), mergeDatabasesRequest.getTo());
    return ResponseEntity.ok(null);
  }
}
