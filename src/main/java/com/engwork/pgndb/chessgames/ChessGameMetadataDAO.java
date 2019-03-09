package com.engwork.pgndb.chessgames;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ChessGameMetadataDAO {

  private UUID id;
  private UUID whitePlayer;
  private UUID blackPlayer;
  private UUID eventId;
  private String result;
  private String description;
  private String additionalInfo;
  private String round;
  private String date;

}
