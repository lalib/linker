package com.bilalalp.searcher.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchGeneratedLinkInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.util.JSoapUtil;
import com.bilalalp.searcher.amqp.MessageSender;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatentScopeSearcherService implements SearcherService {

    private static final String SITE_ADDRESS = "https://patentscope.wipo.int/search/en/";

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private MessageSender messageSender;

    @Qualifier(value = "foundLinkQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void search(final LinkSearchPageInfo linkSearchPageInfo) {

        final LinkSearchRequestInfo linkSearchRequestInfo = linkSearchPageInfo.getLinkSearchRequestInfo();
        final String generatedLink = getGeneratedLink(linkSearchRequestInfo);
        Integer startPage = linkSearchPageInfo.getStartPage();
        final Integer endPage = linkSearchPageInfo.getEndPage();
        int tryCount = 0;

        final List<PatentInfo> patentInfoList = new ArrayList<>();

        Integer pageNumber = startPage;
        pageNumber++;

        boolean eof = false;
        while (!eof) {

            try {

                final String link = generatedLink + pageNumber;
                final Element body = JSoapUtil.getBody(link);
                final Integer pageCount = getPageCount(body);

                if (pageCount <= pageNumber * 10 || pageNumber.equals(endPage)) {
                    eof = true;
                }

                pageNumber++;

                final Elements tables = body.getElementsByTag("table");

                for (final Element table : tables) {

                    final Element resultTable = table.getElementById("resultTable");

                    if (resultTable == null) {
                        continue;
                    }

                    final Elements trElements = table.getElementsByTag("tr");

                    for (final Element element : trElements) {

                        if (doesRowContainsUrl(element)) {
                            final Elements aClass = element.getElementsByAttributeValue("class", "trans-section");

                            final PatentInfo patentInfo = getPatentInfo(element, aClass);
                            if (patentInfo == null) {
                                continue;
                            }

                            patentInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
                            patentInfo.setSearchLink(link);
                            patentInfoList.add(patentInfo);
                        }
                    }
                }

                tryCount = 0;

            } catch (final Exception ex) {

                ex.printStackTrace();
                tryCount++;
                pageNumber--;

                if (Integer.valueOf(100).equals(tryCount)) {
                    break;
                }

                JSoapUtil.sleep();
            }
        }

        patentInfoService.save(patentInfoList);
        messageSender.sendMessage(queueConfigurationDto,convertPatentInfoToQueueMessageDto(patentInfoList));
    }

    private List<QueueMessageDto> convertPatentInfoToQueueMessageDto(final List<PatentInfo> patentInfoList) {

        final List<QueueMessageDto> queueMessageDtoList = new ArrayList<>();

        for (final PatentInfo patentInfo : patentInfoList) {
            queueMessageDtoList.add(new QueueMessageDto(patentInfo.getId()));
        }
        return queueMessageDtoList;
    }

    private PatentInfo getPatentInfo(Element element, Elements aClass) {
        final PatentInfo patentInfo = new PatentInfo();

        if (aClass != null && !aClass.isEmpty()) {
            final String text = aClass.get(0).text();
            patentInfo.setPatentTitle(text);
        } else {
            return null;
        }

        final Elements aElements = element.getElementsByTag("a");

        if (aElements != null && !aElements.isEmpty()) {

            final String href = aElements.get(0).attr("href");
            patentInfo.setPatentLink(SITE_ADDRESS + href);
        } else {
            return null;
        }

        return patentInfo;
    }

    private String getGeneratedLink(final LinkSearchRequestInfo linkSearchRequestInfo) {

        try {
            final List<LinkSearchGeneratedLinkInfo> linkSearchGeneratedLinkInfoList = linkSearchRequestInfo.getLinkSearchGeneratedLinkInfoList();

            for (final LinkSearchGeneratedLinkInfo linkSearchGeneratedLinkInfo : linkSearchGeneratedLinkInfoList) {
                if (SiteInfoType.PATENTSCOPE.equals(linkSearchGeneratedLinkInfo.getSiteInfoType())) {
                    return linkSearchGeneratedLinkInfo.getGeneratedLink();
                }
            }
        } catch (final Exception e) {
            System.out.printf(e.getMessage());
        }

        return null;
    }

    private boolean doesRowContainsUrl(Element element) {

        final Elements td = element.getElementsByTag("a");
        return td != null && !td.isEmpty();
    }

    private Integer getPageCount(Element element) {

        final Elements elementsByAttributeValue = element.getElementsByAttributeValue("class", "topResultFormCol1");
        if (elementsByAttributeValue != null) {
            return Integer.valueOf(elementsByAttributeValue.get(0).getElementsByTag("b").get(1).text().replace(",", ""));
        }
        return 0;
    }
}