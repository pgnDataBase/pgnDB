package com.engwork.pgndb.searching.headerssearching.headerfieldssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.chessgames.ChessGamesMapper;
import com.engwork.pgndb.chessgamesconverter.AdditionalInfoConverter;
import com.engwork.pgndb.databasemanager.DatabaseContainer;
import com.engwork.pgndb.searching.IntersectionResolverBean;
import com.engwork.pgndb.searching.requestmodel.HeaderFilter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
class AdditionalSearchingBeanImpl implements AdditionalSearchingBean {
  private final SqlSessionFactory sqlSessionFactory;
  private final DatabaseContainer databaseContainer;
  private final IntersectionResolverBean<ChessGameMetadataDAO> intersectionResolverBean;

  @Override
  public List<ChessGameMetadataDAO> searchByAdditionalPlayer(String databaseName, Map<String, String> additionalTags) {
    databaseContainer.setDatabaseKey(databaseName);
    List<ChessGameMetadataDAO> result = null;

    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      ChessGamesMapper chessGamesMapper = sqlSession.getMapper(ChessGamesMapper.class);
      for (String additional : additionalTags.keySet()) {
        List<ChessGameMetadataDAO> partialResult = chessGamesMapper.selectChessGamesByAdditionalTag(additional + AdditionalInfoConverter.K_V_SEP);
        // No such tag
        if (partialResult.isEmpty()) {
          return Collections.emptyList();
        }
        // Tag exists
        else {
          partialResult = partialResult.stream().filter(item -> {
            Map<String, String> itemAdditional = AdditionalInfoConverter.fromStringToMap(item.getAdditionalInfo());
            return itemAdditional.get(additional).toLowerCase().contains(additionalTags.get(additional).toLowerCase());
          }).collect(Collectors.toList());
          if (result == null) {
            result = partialResult;
          } else {
            result = intersectionResolverBean.resolve(result, partialResult);
            if (result.isEmpty()) {
              return Collections.emptyList();
            }
          }
        }
      }
      return result;
    }
  }

  @Override
  public List<ChessGameMetadataDAO> execute(String databaseName, HeaderFilter headerFilter) {
    return searchByAdditionalPlayer(databaseName, headerFilter.getAdditional());
  }

  @Override
  public Boolean appliesTo(HeaderFilter headerFilter) {
    Map<String, String> additionals = headerFilter.getAdditional();
    if (additionals == null) {
      return false;
    }
    return !additionals.isEmpty();
  }
}