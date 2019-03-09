package com.engwork.pgndb.export;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ChessGamesExportRequest {
    private String databaseName;
    private List<UUID> gamesIds;
    private boolean getAllGames;
    private String fileName;

}
