package com.engwork.pgndb.chessengine;

import com.engwork.pgndb.chessengine.engineinfo.EngineInfo;
import com.engwork.pgndb.chessengine.engineinfo.EngineInfoManagingBean;
import com.engwork.pgndb.security.JWTValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
class ChessEngineController {
  private final ChessEngineManagingBean chessEngineManagingBean;
  private final EngineInfoManagingBean engineInfoManagingBean;

  private final JWTValidator validator;

  @GetMapping("/engines")
  public ResponseEntity<Response.EnginesResponse> getEngines(@RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    validator.validate(token);
    Response.EnginesResponse enginesResponse = chessEngineManagingBean.getEngines();
    return ResponseEntity.ok(enginesResponse);
  }

  @PostMapping("/engine/evaluate")
  public ResponseEntity startEvaluatingPosition(@RequestBody Request.StartPositionRequest startPositionRequest,
                                                @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    validator.validate(token);
    Integer time = startPositionRequest.getSeconds() * 1000;
    chessEngineManagingBean.startEngine(startPositionRequest.getEngine(), startPositionRequest.getFen(), time);
    return ResponseEntity.ok(null);
  }

  @PostMapping("/engine/stop")
  public ResponseEntity<Response.EngineStopResponse> stopEngine(@RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    validator.validate(token);
    Response.EngineStopResponse enginesResponse = chessEngineManagingBean.stopEngine();
    return ResponseEntity.ok(enginesResponse);
  }

  @GetMapping("/engine/result")
  public ResponseEntity<List<EngineInfo>> getResult(@RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    validator.validate(token);
    List<EngineInfo> engineInfoList = engineInfoManagingBean.getEngineInfo();
    engineInfoList = engineInfoList.stream().filter(eng -> eng.getDepth() != null).collect(Collectors.toList());
    return ResponseEntity.ok(engineInfoList);
  }
}
