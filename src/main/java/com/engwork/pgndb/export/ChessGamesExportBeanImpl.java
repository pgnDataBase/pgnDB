package com.engwork.pgndb.export;

import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesconverter.ChessGamesConverter;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesdownloader.ChessGamesDownloaderBean;

import com.engwork.pgndb.databasemanager.DatabaseContainer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChessGamesExportBeanImpl implements ChessGamesExportBean {
    private ChessGamesConverter chessGamesConverter;
    private ChessGamesDownloaderBean chessGamesDownloaderBean;
    private ChessGamesMapper chessGamesMapper;
    private DatabaseContainer databaseContainer;

    @Override
    public InputStream exportGames(ChessGamesExportRequest chessGamesExportRequest){
        String pgnString="";
        List<UUID> gamesIds;
        if(chessGamesExportRequest.isGetAllGames()) {
            databaseContainer.setDatabaseKey(chessGamesExportRequest.getDatabaseName());
            gamesIds = chessGamesMapper.selectChessGamesIds();
        } else {
            gamesIds = chessGamesExportRequest.getGamesIds();
        }
        for(UUID id : gamesIds){
           ChessGameData chessGameData =  chessGamesDownloaderBean.getChessGameDataById(id,chessGamesExportRequest.getDatabaseName());
           pgnString = pgnString.concat(chessGamesConverter.convertChessGameDataIntoPGN(chessGameData)+"\n");
        }
        return new ByteArrayInputStream(pgnString.getBytes(StandardCharsets.UTF_8));
    }
}
