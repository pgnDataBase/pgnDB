package com.engwork.pgndb.statusresolver;


import com.engwork.pgndb.accessauthorization.AccessAuthorizationBean;
import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.users.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class StatusController {

  private final StatusResolver loadingStatusResolver;
  private final StatusResolver treeBuildingStatusResolver;

  private final AccessAuthorizationBean accessAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;
  private final JWTValidator validator;

  public StatusController(@Qualifier("loading.status.resolver") StatusResolver loadingStatusResolver,
                          @Qualifier("tree.building.status.resolver") StatusResolver treeBuildingStatusResolver,
                          AccessAuthorizationBean accessAuthorizationBean,
                          AccessExceptionsFactory accessExceptionsFactory, JWTValidator validator) {
    this.loadingStatusResolver = loadingStatusResolver;
    this.treeBuildingStatusResolver = treeBuildingStatusResolver;
    this.accessAuthorizationBean = accessAuthorizationBean;
    this.accessExceptionsFactory = accessExceptionsFactory;
    this.validator = validator;
  }

  @PostMapping("/status/loading")
  public ResponseEntity getLoadingStatus(@RequestBody StatusRequest statusRequest,
                                         @RequestHeader("Authorization") String token) {
    return getResponseEntity(statusRequest, token, loadingStatusResolver);
  }

  @PostMapping("/status/building/tree")
  public ResponseEntity getTreeBuildingStatus(@RequestBody StatusRequest statusRequest,
                                              @RequestHeader("Authorization") String token) {
    return getResponseEntity(statusRequest, token, treeBuildingStatusResolver);
  }

  private ResponseEntity getResponseEntity(@RequestBody StatusRequest statusRequest,
                                           @RequestHeader("Authorization") String token,
                                           StatusResolver treeBuildingStatusResolver) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), statusRequest.getDatabaseName())) {
      throw accessExceptionsFactory.noAccessWithName(statusRequest.getDatabaseName());
    }

    Status result = treeBuildingStatusResolver.getStatusByKey(statusRequest.getDatabaseName());
    if (result == null) {
      return ResponseEntity.ok(0);
    } else {
      return ResponseEntity.ok(result.getValue().intValue());
    }
  }

}
