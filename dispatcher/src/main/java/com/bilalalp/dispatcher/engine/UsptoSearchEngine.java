package com.bilalalp.dispatcher.engine;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsptoSearchEngine extends AbstractSearchEngine implements SearchEngine {

    private static final Integer PER_PAGE_RECORD_COUNT = 50;

    @Override
    protected String generateLink(final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList) {
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
    protected SiteInfoType getSiteInfoType() {
        return SiteInfoType.USPTO;
    }
}