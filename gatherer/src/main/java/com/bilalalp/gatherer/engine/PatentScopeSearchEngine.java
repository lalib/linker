package com.bilalalp.gatherer.engine;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatentScopeSearchEngine extends AbstractSearchEngine implements SearchEngine {

    private static final Integer PER_PAGE_RECORD_COUNT = 10;

    @Override
    public String generateLink(final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://patentscope.wipo.int/search/en/result.jsf?query=");

        int index = 0;

        for (final LinkSearchRequestKeywordInfo keywordInfoDto : linkSearchRequestKeywordInfoList) {
            index++;
            final String replacedKeyword = keywordInfoDto.getKeyword().replace(" ", "%20");
            stringBuilder
                    .append("FP:(").append(replacedKeyword)
                    .append(")%20OR%20").append("EN_AB:(")
                    .append(replacedKeyword).append(")%20OR%20")
                    .append("EN_CL:(").append(replacedKeyword)
                    .append(")%20OR%20").append("EN_DE:(")
                    .append(replacedKeyword).append(")%20OR%20")
                    .append("PA:(").append(replacedKeyword)
                    .append(")%20OR%20").append("EN_ALLTXT:(")
                    .append(replacedKeyword).append(")%20OR%20")
                    .append("EN_TI:(").append(replacedKeyword)
                    .append(")");

            if (linkSearchRequestKeywordInfoList.size() == index) {
                stringBuilder.append("%20&");
            }
        }

        return stringBuilder
                .append("sortOption=Relevance&viewOption=Simple&currentNavigationRow=")
                .toString();
    }

    @Override
    protected List<String> generatedLinkList(List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList) {
        return null;
    }

    @Override
    protected Integer getPageCount(final Element element) {

        final Elements elementsByAttributeValue = element.getElementsByAttributeValue("class", "topResultFormCol1");
        if (elementsByAttributeValue != null) {
            return Integer.valueOf(elementsByAttributeValue.get(0).getElementsByTag("b").get(1).text().replace(",", ""));
        }
        return 0;
    }

    @Override
    protected Integer getPerPageRecordCount() {
        return PER_PAGE_RECORD_COUNT;
    }

    @Override
    protected SearchType getSearchType() {
        return SearchType.ALL;
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.PATENTSCOPE;
    }
}