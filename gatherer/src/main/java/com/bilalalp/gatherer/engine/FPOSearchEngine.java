package com.bilalalp.gatherer.engine;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FPOSearchEngine extends AbstractSearchEngine implements SearchEngine {

    private static final Integer PER_PAGE_RECORD_COUNT = 50;

    @Override
    protected String generateLink(final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://www.freepatentsonline.com/result.html?sort=relevance&search=Search");
        stringBuilder.append("&edit_alert=&srch=xprtsrch&depat=on&date_range=all&stemming=on");
        stringBuilder.append("&uspat=on&usapp=on&eupat=on&jp=on&pct=on");
        stringBuilder.append("&query_txt=");

        boolean firstTime = true;

        for (final LinkSearchRequestKeywordInfo linkSearchRequestKeywordInfo : linkSearchRequestKeywordInfoList) {

            final String replacedKeyword = linkSearchRequestKeywordInfo.getKeyword().replace(" ", "+");

            if (firstTime) {
                stringBuilder.append("ABST%2F%22").append(replacedKeyword).append("%22");
            } else {
                stringBuilder.append("+or+ABST%2F%22").append(replacedKeyword).append("%22");
            }

            stringBuilder.append("+or+ACLM%2F%22").append(replacedKeyword).append("%22");
            stringBuilder.append("+or+TTL%2F%22").append(replacedKeyword).append("%22");
            stringBuilder.append("+or+SPEC%2F%22").append(replacedKeyword).append("%22");
            firstTime = false;
        }

        stringBuilder.append("&p=1");
        return stringBuilder.toString();
    }

    @Override
    protected Integer getPageCount(final Element element) {
        final String replacedText = element.getElementsByAttributeValue("class", "well well-small").get(0).getElementsContainingOwnText("Matches").text().replace("Matches 1 - 50 out of ", "");
        return Integer.valueOf(replacedText);
    }

    @Override
    protected Integer getPerPageRecordCount() {
        return PER_PAGE_RECORD_COUNT;
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.FPO;
    }
}
