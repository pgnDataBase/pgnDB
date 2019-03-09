package com.engwork.pgndb.export;

import java.io.InputStream;

public interface ChessGamesExportBean {
    InputStream exportGames(ChessGamesExportRequest chessGamesExportRequest);
}
