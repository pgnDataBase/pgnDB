package com.engwork.pgndb.searching.headerssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.searching.IntersectionResolverBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.HeaderFieldSearchingBean;
import com.engwork.pgndb.searching.requestmodel.HeaderFilter;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class HeadersSearchingBeanImpl implements HeadersSearchingBean {
  private final List<HeaderFieldSearchingBean> headerFieldSearchingBeans;
  private final IntersectionResolverBean<ChessGameMetadataDAO> intersectionResolverBean;

  @Override
  public List<ChessGameMetadataDAO> search(String databaseName, HeaderFilter headerFilter) {

    List<ChessGameMetadataDAO> result = null;
    for (HeaderFieldSearchingBean headerFieldSearchingBean : headerFieldSearchingBeans) {
      if (headerFieldSearchingBean.appliesTo(headerFilter)) {
        List<ChessGameMetadataDAO> partialResult = headerFieldSearchingBean.execute(databaseName, headerFilter);
        if (partialResult.isEmpty()) {
          return Collections.emptyList();
        } else {
          if (result == null) {
            result = partialResult;
          } else {
            result = intersectionResolverBean.resolve(result, partialResult);
          }
        }
      }
    }
    if (result == null) {
      return Collections.emptyList();
    } else {
      return result;
    }
  }
}
