package com.engwork.pgndb.moveediting;

import com.engwork.pgndb.chessgamesconverter.model.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewMoveRequest {
  private Move previousMove;
  private Move newMove;
}
