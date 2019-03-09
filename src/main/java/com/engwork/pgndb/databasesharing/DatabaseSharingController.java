package com.engwork.pgndb.databasesharing;

import com.engwork.pgndb.accessauthorization.AccessAuthorizationBean;
import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.accessauthorization.OwnershipAuthorizationBean;
import com.engwork.pgndb.accessauthorization.SharingAuthorizationBean;
import com.engwork.pgndb.databasesharing.requestmodel.AccessRequest;
import com.engwork.pgndb.databasesharing.requestmodel.IsSharedRequest;
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
public class DatabaseSharingController {

  private final DatabaseSharingBean databaseSharingBean;
  private final JWTValidator validator;

  private final SharingAuthorizationBean sharingAuthorizationBean;
  private final OwnershipAuthorizationBean ownershipAuthorizationBean;
  private final AccessAuthorizationBean accessAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;

  @PostMapping("/chessdb/access")
  public ResponseEntity addChessDatabaseAccess(@RequestBody AccessRequest accessRequest,
                                               @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    databaseSharingBean.addAccess(user.getId(), accessRequest.getDatabaseId(), accessRequest.getUsername());
    return ResponseEntity.ok(null);
  }

  @PostMapping("/chessdb/access/remove")
  public ResponseEntity removeChessDatabaseAccess(@RequestBody AccessRequest accessRequest,
                                                  @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), accessRequest.getDatabaseId())) {
      throw accessExceptionsFactory.noAccessWithId(accessRequest.getDatabaseId());
    }

    databaseSharingBean.removeAccess(accessRequest.getDatabaseId(), accessRequest.getUsername());
    return ResponseEntity.ok(null);
  }

  @PostMapping("/chessdb/access/users")
  public ResponseEntity getDatabaseAccesses(@RequestBody IsSharedRequest isSharedRequest,
                                            @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), isSharedRequest.getDatabaseId())) {
      throw accessExceptionsFactory.noAccessWithId(isSharedRequest.getDatabaseId());
    }
    return ResponseEntity.ok(databaseSharingBean.getUsersWithAccess(user.getId(), isSharedRequest.getDatabaseId()));
  }
}
