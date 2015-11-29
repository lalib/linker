package com.bilalalp.searcher.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchGeneratedLinkInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.util.JSoupUtil;
import com.bilalalp.searcher.amqp.MessageSender;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsptoSearcherService implements SearcherService {

    private static final String MAIN_URL = "http://patft.uspto.gov";

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private MessageSender messageSender;


    @Qualifier(value = "foundLinkQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Override
    public void search(final LinkSearchPageInfo linkSearchPageInfo) {

        final Integer startPage = linkSearchPageInfo.getStartPage();
        final Integer endPage = linkSearchPageInfo.getEndPage();
        final String generatedLink = getGeneratedLink(linkSearchPageInfo.getLinkSearchRequestInfo());
        final List<PatentInfo> patentInfoList = new ArrayList<>();

        int tryCount = 0;

        for (int i = startPage + 1; i < endPage + 1; i++) {

            try {

                final String link = generatedLink + i;
                final Element body = JSoupUtil.getBody(link);

                if (body == null) {
                    throw new RuntimeException("Body is null!");
                }
                final Elements table = body.getElementsByTag("table");

                boolean isItMiddle = false;

                for (Element element : table) {

                    if (!isItMiddle) {
                        isItMiddle = true;
                        continue;
                    }

                    final Elements tr = element.getElementsByTag("tr");

                    for (Element trElement : tr) {
                        final Elements aElements = trElement.getElementsByTag("a");

                        if (aElements != null && !aElements.isEmpty()) {
                            final Element element1 = aElements.get(0);
                            final Element element2 = aElements.get(1);

                            final PatentInfo patentInfo = new PatentInfo();
                            patentInfo.setPatentLink(MAIN_URL + element1.attr("href"));
                            patentInfo.setPatentNumber(element1.text());
                            patentInfo.setPatentTitle(element2.text());
                            patentInfo.setSearchLink(link);
                            patentInfo.setLinkSearchPageInfo(linkSearchPageInfo);
                            patentInfoList.add(patentInfo);
                        }
                    }

                    isItMiddle = false;
                }

                tryCount = 0;
            } catch (final Exception ex) {
                ex.printStackTrace();
                tryCount++;
                i--;

                if (Integer.valueOf(100).equals(tryCount)) {
                    break;
                }

                JSoupUtil.sleep();
            }
        }

        patentInfoService.save(patentInfoList);
        messageSender.sendMessage(queueConfigurationDto, convertPatentInfoToQueueMessageDto(patentInfoList));
    }


    private List<QueueMessageDto> convertPatentInfoToQueueMessageDto(final List<PatentInfo> patentInfoList) {
        return patentInfoList.stream().map(patentInfo -> new QueueMessageDto(patentInfo.getId())).collect(Collectors.toList());
    }

    private String getGeneratedLink(final LinkSearchRequestInfo linkSearchRequestInfo) {

        try {
            final List<LinkSearchGeneratedLinkInfo> linkSearchGeneratedLinkInfoList = linkSearchRequestInfo.getLinkSearchGeneratedLinkInfoList();

            for (final LinkSearchGeneratedLinkInfo linkSearchGeneratedLinkInfo : linkSearchGeneratedLinkInfoList) {
                if (SiteInfoType.USPTO.equals(linkSearchGeneratedLinkInfo.getSiteInfoType())) {
                    return linkSearchGeneratedLinkInfo.getGeneratedLink().replace("p=1", "p=");
                }
            }
        } catch (final Exception e) {
            System.out.printf(e.getMessage());
        }

        return null;
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.USPTO;
    }
}