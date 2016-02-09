package com.bilalalp.searcher.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.exception.LinkerCommonException;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.util.JSoupUtil;
import com.bilalalp.searcher.amqp.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FpoSearcherService implements SearcherService {

    private static final String MAIN_URL = "http://www.freepatentsonline.com";

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
        final String generatedLink = linkSearchPageInfo.getGeneratedLink().replace("p=1", "p=");
        final List<PatentInfo> patentInfoList = new ArrayList<>();

        int tryCount = 0;

        for (int i = startPage + 1; i < endPage + 1; i++) {

            try {

                final String link = generatedLink + i;
                final Element body = JSoupUtil.getBody(link);

                if (body == null) {
                    throw new LinkerCommonException("Body is null!");
                } else if (body.html().contains("Your search returned no results.")) {
                    continue;
                }

                final Elements table = body.getElementsByAttributeValue("class", "listing_table");
                final Element element = table.get(0);

                final Elements children = element.children();
                final Element tables = children.get(0);
                final Elements elements = tables.children();

                boolean firstOne = true;

                for (final Element el : elements) {

                    if (firstOne) {
                        firstOne = false;
                        continue;
                    }

                    final Elements links = el.children();
                    final Element foundLinkNode = links.get(2);
                    final Elements elementsByTag = foundLinkNode.getElementsByTag("a");
                    final Element foundLink = elementsByTag.get(0);
                    final String href = foundLink.attr("href");
                    final String mainUrl = MAIN_URL + href;
                    final PatentInfo patentInfo = new PatentInfo();
                    patentInfo.setLinkSearchPageInfo(linkSearchPageInfo);
                    patentInfo.setPatentTitle(elementsByTag.text());
                    patentInfo.setPatentLink(mainUrl);
                    patentInfo.setSearchLink(link);
                    patentInfoList.add(patentInfo);
                }

                tryCount = 0;
            } catch (final Exception ex) {
                log.error(ex.getMessage(), ex);
                tryCount++;
                i--;

                if (Integer.valueOf(100).equals(tryCount)) {
                    break;
                }

                JSoupUtil.sleep();
            }
        }

        if (CollectionUtils.isNotEmpty(patentInfoList)) {
            patentInfoService.save(patentInfoList);
            messageSender.sendMessage(queueConfigurationDto, convertPatentInfoToQueueMessageDto(patentInfoList));
        }
    }

    private List<QueueMessageDto> convertPatentInfoToQueueMessageDto(final List<PatentInfo> patentInfoList) {
        return patentInfoList.stream().map(patentInfo -> new QueueMessageDto(patentInfo.getId())).collect(Collectors.toList());
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.FPO;
    }
}