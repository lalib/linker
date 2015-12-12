package com.bilalalp.gatherer.engine;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    protected List<String> generatedLinkList(List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList) {

        final Calendar startCalendar = getStartCalendar();

        final Calendar endCalendar = Calendar.getInstance();

        final List<String> generatedLinks = new ArrayList<>();

        while (startCalendar.before(endCalendar)) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://www.freepatentsonline.com/result.html?sort=relevance&search=Search");
            stringBuilder.append("&edit_alert=&srch=xprtsrch&depat=on&date_range=all&stemming=on");
            stringBuilder.append("&uspat=on&usapp=on&eupat=on&jp=on&pct=on");
            stringBuilder.append("&query_txt=");

            boolean firstTime = true;

            for (final LinkSearchRequestKeywordInfo linkSearchRequestKeywordInfo : linkSearchRequestKeywordInfoList) {

                final String replacedKeyword = linkSearchRequestKeywordInfo.getKeyword().replace(" ", "+");

                if (firstTime) {
                    stringBuilder.append("%28ABST%2F%22").append(replacedKeyword).append("%22");
                } else {
                    stringBuilder.append("+or+ABST%2F%22").append(replacedKeyword).append("%22");
                }

                stringBuilder.append("+or+ACLM%2F%22").append(replacedKeyword).append("%22");
                stringBuilder.append("+or+TTL%2F%22").append(replacedKeyword).append("%22");
                stringBuilder.append("+or+SPEC%2F%22").append(replacedKeyword).append("%22");
                firstTime = false;
            }

            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            final String formattedStartDate = getFormattedDate(startCalendar);
            startCalendar.add(Calendar.MONTH, 1);
            final String formattedEndDate = getFormattedDate(startCalendar);

            stringBuilder.append("%29+AND+APD%2F");
            stringBuilder.append(formattedStartDate);
            stringBuilder.append("-%3E");
            stringBuilder.append(formattedEndDate);
            stringBuilder.append("&p=1");
            generatedLinks.add(stringBuilder.toString());

        }
        return generatedLinks;
    }

    private String getFormattedDate(final Calendar calendar) {

        final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        final String formattedDate = df.format(calendar.getTime());
        return formattedDate.replaceAll("/", "%2F");
    }

    private Calendar getStartCalendar() {
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.MONTH, 1);
        startCalendar.set(Calendar.YEAR, 1975);
        return startCalendar;
    }

    @Override
    protected Integer getPageCount(final Element element) {
        try {
            final String replacedText = element.getElementsByAttributeValue("class", "well well-small").get(0).getElementsContainingOwnText("Matches").text().replace("Matches 1 - ", "");
            final String[] split = replacedText.split(" ");
            final String s = split[split.length - 1];
            return Integer.valueOf(s);
        } catch (final Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    protected Integer getPerPageRecordCount() {
        return PER_PAGE_RECORD_COUNT;
    }

    @Override
    protected SearchType getSearchType() {
        return SearchType.MONTHLY;
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.FPO;
    }
}
