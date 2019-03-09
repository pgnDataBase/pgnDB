package com.engwork.pgndb.chessdatabases;

import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.accessauthorization.OwnershipAuthorizationBean;
import com.engwork.pgndb.chessdatabases.requestmodel.ChessDatabaseCreateRequest;
import com.engwork.pgndb.databasemanager.DatabaseManager;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.users.User;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
class ChessDatabasesController {

  private final ChessDatabasesManagementBean chessDatabasesManagementBean;
  private final DatabaseManager databaseManager;

  private final JWTValidator validator;
  private final OwnershipAuthorizationBean ownershipAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;

  @GetMapping(value = "/chessdbs")
  public ResponseEntity getChessDatabases(@RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);

    List<ChessDatabase> response = chessDatabasesManagementBean.getChessDatabasesByUserId(user.getId());
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/chessdb")
  public ResponseEntity createChessDatabase(@RequestBody ChessDatabaseCreateRequest chessDatabaseCreateRequest,
                                            @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);

    databaseManager.create(chessDatabaseCreateRequest.getDatabaseName(), user.getId());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(value = "/chessdb")
  public ResponseEntity deleteChessDatabase(@RequestParam("databaseName") String databaseName,
                                            @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);

    if (!ownershipAuthorizationBean.validateAccess(user.getId(), databaseName)) {
      throw accessExceptionsFactory.notOwnerWithName(databaseName);
    }
    databaseManager.delete(databaseName, true);
    return ResponseEntity.ok().build();
  }
}
