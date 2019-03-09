package com.engwork.pgndb.moveediting;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
class MoveEditingController {
  private final MoveValidatingBean moveValidatingBean;
  private final MoveSavingBean moveSavingBean;

  @PostMapping(value = "/move/create")
  public ResponseEntity<Move> createMove(@RequestBody NewMoveRequest newMoveRequest) {
    return ResponseEntity.ok(moveValidatingBean.parseMove(newMoveRequest));
  }

  @PostMapping(value = "/moves/save")
  public ResponseEntity<SaveChangesResponse> saveMoves(@RequestBody SaveChangesRequest saveChangesRequest) {
    SaveChangesResponse saveChangesResponse = moveSavingBean.saveChanges(saveChangesRequest);
    return ResponseEntity.ok(saveChangesResponse);
  }
}
