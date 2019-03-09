package com.engwork.pgndb.chessgames;

import com.engwork.pgndb.accessauthorization.AccessAuthorizationBean;
import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.chessgames.ChessGamesRequest.ChessGameRequest;
import com.engwork.pgndb.chessgames.ChessGamesRequest.ChessGamesMetadataRequest;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesdownloader.ChessGamesDownloaderBean;
import com.engwork.pgndb.chessgameseditor.ChessGamesEditorBean;
import com.engwork.pgndb.exceptionhandling.ValidationException;
import com.engwork.pgndb.searching.GamesSearchingBean;
import com.engwork.pgndb.searching.requestmodel.GamesSearchingFilter;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatusBean;
import com.engwork.pgndb.users.User;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
class ChessGamesController {

  private final ChessGamesBean chessGamesBean;
  private final GamesSearchingBean gamesSearchingBean;
  private final ChessGamesEditorBean chessGamesEditorBean;
  private final ChessGamesDownloaderBean chessGamesDownloaderBean;
  private final TreeStatusBean treeStatusBean;

  private final AccessAuthorizationBean accessAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;
  private final JWTValidator validator;

  @PostMapping(value = "/games")
  public ResponseEntity getChessGames(@RequestBody ChessGamesMetadataRequest chessGamesMetadataRequest,
                                      @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (chessGamesMetadataRequest.getPageSize() < 1) {
      throw ValidationException.forSingleObjectAndFieldAndReason("Chess Game Request", "Page Size", "Is less than one");
    }
    if (!accessAuthorizationBean.validateAccess(user.getId(), chessGamesMetadataRequest.getDatabaseName())) {
      throw accessExceptionsFactory.noAccessWithName(chessGamesMetadataRequest.getDatabaseName());
    }

    ChessGamesMetadataResponse response;
    GamesSearchingFilter gamesSearchingFilter = chessGamesMetadataRequest.getFilter();
    boolean isFiltering = gamesSearchingFilter != null;
    if (isFiltering) {
      response = gamesSearchingBean.search(chessGamesMetadataRequest);
    } else {
      response = chessGamesBean.getChessGames(chessGamesMetadataRequest);
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/game/{chessGameId}")
  public ResponseEntity getChessGameById(@PathVariable("chessGameId") UUID chessGameId,
                                         @RequestBody ChessGameRequest chessGameRequest,
                                         @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), chessGameRequest.getDatabaseName())) {
      throw accessExceptionsFactory.noAccessWithName(chessGameRequest.getDatabaseName());
    }

    ChessGameData response = chessGamesDownloaderBean.getChessGameDataById(chessGameId, chessGameRequest.getDatabaseName());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/game/modify")
  public ResponseEntity editChessGameMetadata(@RequestParam("databaseName") String databaseName,
                                              @RequestBody ChessGameMetadata chessGameMetadata,
                                              @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), databaseName)) {
      throw accessExceptionsFactory.noAccessWithName(databaseName);
    }

    chessGamesEditorBean.updateChessGameMetadata(databaseName, chessGameMetadata);
    return ResponseEntity.ok(null);
  }

  @PostMapping(value = "/games/delete")
  public ResponseEntity deleteChessGames(@RequestBody DeleteGamesRequest deleteGamesRequest,
                                         @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), deleteGamesRequest.getDatabaseName())) {
      throw accessExceptionsFactory.noAccessWithName(deleteGamesRequest.getDatabaseName());
    }

    chessGamesEditorBean.deleteGames(deleteGamesRequest);
    treeStatusBean.setTreeNotUpToDate(deleteGamesRequest.getDatabaseName());
    return ResponseEntity.ok(null);
  }
}
