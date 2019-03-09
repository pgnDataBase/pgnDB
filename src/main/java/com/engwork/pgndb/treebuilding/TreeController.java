package com.engwork.pgndb.treebuilding;

import com.engwork.pgndb.accessauthorization.AccessAuthorizationBean;
import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.exceptionhandling.ValidationException;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.treebuilding.requestmodel.GetTreeNodeRequest;
import com.engwork.pgndb.treebuilding.requestmodel.PostTreeRequest;
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
class TreeController {
  private final TreeBuildingBean treeBuildingBean;
  private final TreeSearchingBean treeSearchingBean;

  private final AccessAuthorizationBean accessAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;
  private final JWTValidator validator;


  @PostMapping(value = "/tree")
  public ResponseEntity buildTree(@RequestBody PostTreeRequest postTreeRequest,
                                  @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), postTreeRequest.getDatabaseName())) {
      throw accessExceptionsFactory.noAccessWithName(postTreeRequest.getDatabaseName());
    }

    log.info("Started tree building for database {}.", postTreeRequest.getDatabaseName());
    treeBuildingBean.buildTree(postTreeRequest.getDatabaseName(), postTreeRequest.getIncludeVariants());
    log.info("Tree building for database {} succeeded.", postTreeRequest.getDatabaseName());
    return ResponseEntity.ok(null);
  }

  @PostMapping(value = "/search/tree")
  public ResponseEntity getTreeNodes(@RequestBody GetTreeNodeRequest getTreeNodeRequest,
                                     @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), getTreeNodeRequest.getDatabaseName())) {
      throw accessExceptionsFactory.noAccessWithName(getTreeNodeRequest.getDatabaseName());
    }

    if (getTreeNodeRequest.getPositionId() == null ^ getTreeNodeRequest.getFen() == null) {
      return ResponseEntity.ok(treeSearchingBean.search(getTreeNodeRequest));
    } else {
      throw ValidationException.forSingleObjectAndFieldAndReason("TreeSearchingBean",
                                                                 "positionId/fen",
                                                                 "You must provide one of: [positionId, fen].");
    }
  }
}
