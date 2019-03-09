package com.engwork.pgndb.export;

import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ChessGamesExportController {
  private ChessGamesExportBean chessGamesExportBean;


  @PostMapping(value = "/games/export")
  public ResponseEntity<Resource> exportChessGames(@RequestBody ChessGamesExportRequest chessGamesExportRequest, HttpServletResponse response) {
    InputStream inputStream = chessGamesExportBean.exportGames(chessGamesExportRequest);
    Resource resource = new InputStreamResource(inputStream);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + chessGamesExportRequest.getFileName() + "\"")
        .body(resource);
  }
}
