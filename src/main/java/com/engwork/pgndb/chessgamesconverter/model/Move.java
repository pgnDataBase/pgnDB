package com.engwork.pgndb.chessgamesconverter.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Move {
  private UUID id;
  private String fromField;
  private String toField;
  private UUID gameId;
  private String moveType;
  private String pieceCode;
  private String capturedPieceCode = null;
  private String promotedPieceCode = null;
  private Integer moveNumber;
  private Integer variantId = null;
  private String comment = null;
  private UUID positionId = null;
  private String fen;
  private String variantType = null;
  private Integer entityNumber = 0;
  private Boolean promotionAllowed = false;
  private String san;

  public boolean isVariant() {
    return variantId != null;
  }

  public boolean isFirstMove() {
    return entityNumber != null && entityNumber.equals(1);
  }

  public boolean isNotNullMove() {
    return !moveType.equals(MoveType.VEME.name());
  }
}
