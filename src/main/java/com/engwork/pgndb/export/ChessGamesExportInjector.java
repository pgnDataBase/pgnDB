package com.engwork.pgndb.export;

import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesconverter.ChessGamesConverter;
import com.engwork.pgndb.chessgamesdownloader.ChessGamesDownloaderBean;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChessGamesExportInjector {

    @Autowired
    public ChessGamesExportInjector(ChessGamesConverter chessGamesConverter, ChessGamesDownloaderBean chessGamesDownloaderBean, ChessGamesMapper chessGamesMapper, DatabaseContainer databaseContainer) {
        this.chessGamesConverter = chessGamesConverter;
        this.chessGamesDownloaderBean = chessGamesDownloaderBean;
        this.chessGamesMapper = chessGamesMapper;
        this.databaseContainer = databaseContainer;
    }

    @Bean
    public ChessGamesExportBean chessGamesExportBean(){
        return new ChessGamesExportBeanImpl(chessGamesConverter,chessGamesDownloaderBean,chessGamesMapper,databaseContainer);
    }

    private final ChessGamesConverter chessGamesConverter;
    private final ChessGamesDownloaderBean chessGamesDownloaderBean;
    private final ChessGamesMapper chessGamesMapper;
    private final DatabaseContainer databaseContainer;
}
