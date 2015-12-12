package com.bilalalp.gatherer.engine;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsptoSearchEngine extends AbstractSearchEngine {

    private static final Integer PER_PAGE_RECORD_COUNT = 50;

    @Override
    protected String generateLink(final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://patft.uspto.gov/netacgi/nph-Parser");
        stringBuilder.append("?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-adv.htm&r=0&f=S&l=50");
        stringBuilder.append("&Query=");

        boolean firstTime = true;
        for (final LinkSearchRequestKeywordInfo linkSearchRequestKeywordInfo : linkSearchRequestKeywordInfoList) {
            final String keyword = linkSearchRequestKeywordInfo.getKeyword().trim().replaceAll(" ", "+");

            if (firstTime) {
                stringBuilder.append("ACLM%2F%22");
            } else {
                stringBuilder.append("+OR+ACLM%2F%22");
            }

            stringBuilder.append(keyword).append("%22");
            stringBuilder.append("+OR+ABST%2F%22").append(keyword).append("%22");
            stringBuilder.append("+OR+TTL%2F%22").append(keyword).append("%22");
            stringBuilder.append("+OR+SPEC%2F%22").append(keyword).append("%22");
            firstTime = false;
        }

        stringBuilder.append("&d=PTXT").append("&p=1");
        return stringBuilder.toString();
    }

    @Override
    protected List<String> generatedLinkList(List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList) {
        return null;
    }

    @Override
    protected Integer getPageCount(final Element body) {

        final Elements elementsByTag = body.getElementsByTag("i");

        for (Element element : elementsByTag) {
            final Elements elementsContainingText = element.getElementsContainingText("Hits 1 through 50");
            if (!elementsContainingText.isEmpty()) {
                final Element element1 = elementsContainingText.get(0);
                return Integer.valueOf(element1.getElementsByTag("strong").get(2).text());
            }
        }
        return null;
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
        return SiteInfoType.USPTO;
    }
}