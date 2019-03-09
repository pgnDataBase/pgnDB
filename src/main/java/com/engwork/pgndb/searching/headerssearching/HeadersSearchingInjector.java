package com.engwork.pgndb.searching.headerssearching;

import com.engwork.pgndb.chessgames.ChessGameMetadataDAO;
import com.engwork.pgndb.searching.IntersectionResolverBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.AdditionalSearchingBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.BlackPlayerSearchingBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.DateSearchingBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.EventSearchingBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.HeaderFieldSearchingBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.ResultSearchingBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.RoundSearchingBean;
import com.engwork.pgndb.searching.headerssearching.headerfieldssearching.WhitePlayerSearchingBean;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
class HeadersSearchingInjector {

  private final EventSearchingBean eventSearchingBean;
  private final WhitePlayerSearchingBean whitePlayerSearchingBean;
  private final BlackPlayerSearchingBean blackPlayerSearchingBean;
  private final RoundSearchingBean roundSearchingBean;
  private final ResultSearchingBean resultSearchingBean;
  private final DateSearchingBean dateSearchingBean;
  private final AdditionalSearchingBean additionalSearchingBean;
  private final IntersectionResolverBean<ChessGameMetadataDAO> intersectionResolverBean;

  @Bean
  HeadersSearchingBean getHeadersSearchingBean() {
    List<HeaderFieldSearchingBean> headerFieldSearchingBeans = new ArrayList<>();
    headerFieldSearchingBeans.add(eventSearchingBean);
    headerFieldSearchingBeans.add(whitePlayerSearchingBean);
    headerFieldSearchingBeans.add(blackPlayerSearchingBean);
    headerFieldSearchingBeans.add(roundSearchingBean);
    headerFieldSearchingBeans.add(resultSearchingBean);
    headerFieldSearchingBeans.add(dateSearchingBean);
    headerFieldSearchingBeans.add(additionalSearchingBean);
    return new HeadersSearchingBeanImpl(headerFieldSearchingBeans, intersectionResolverBean);
  }
}
