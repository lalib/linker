package com.bilalalp.dispatcher.engine;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchGeneratedLinkInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.LinkSearchGeneratedLinkInfoService;
import com.bilalalp.common.service.LinkSearchPageInfoService;
import com.bilalalp.dispatcher.amqp.MessageSender;
import com.bilalalp.common.util.JSoupUtil;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSearchEngine {

    private static final Integer SLICE_COUNT = 10;

    @Autowired
    private LinkSearchPageInfoService linkSearchPageInfoService;

    @Autowired
    private LinkSearchGeneratedLinkInfoService linkSearchGeneratedLinkInfoService;

    @Autowired
    private MessageSender messageSender;

    @Qualifier(value = "linkSearcherQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    protected abstract String generateLink(final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList);

    protected abstract Integer getPageCount(Element element);

    protected abstract Integer getPerPageRecordCount();

    protected abstract SiteInfoType getSiteInfoType();

    @Transactional
    public void crawlLink(final LinkSearchRequestInfo linkSearchRequestInfo) {

        final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList = linkSearchRequestInfo.getLinkSearchRequestKeywordInfoList();

        final String generatedLink = generateLink((linkSearchRequestKeywordInfoList));
        final LinkSearchGeneratedLinkInfo linkSearchGeneratedLinkInfo = createLinkSearchGeneratedLinkInfo(linkSearchRequestInfo, generatedLink);
        linkSearchGeneratedLinkInfoService.save(linkSearchGeneratedLinkInfo);

        final Element body = JSoupUtil.getBody(generatedLink);
        final Integer totalPageCount = getPageCount(body);

        final List<LinkSearchPageInfo> linkSearchPageInfoList = getLinkSearchPageInfoList(linkSearchRequestInfo, totalPageCount);
        linkSearchPageInfoService.save(linkSearchPageInfoList);

        final List<QueueMessageDto> queueMessageDtoList = convertEntitiesToMessages(linkSearchPageInfoList);
        messageSender.sendMessage(queueConfigurationDto, queueMessageDtoList);
    }

    private LinkSearchGeneratedLinkInfo createLinkSearchGeneratedLinkInfo(final LinkSearchRequestInfo linkSearchRequestInfo, final String generatedLink) {
        final LinkSearchGeneratedLinkInfo linkSearchGeneratedLinkInfo = new LinkSearchGeneratedLinkInfo();
        linkSearchGeneratedLinkInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
        linkSearchGeneratedLinkInfo.setGeneratedLink(generatedLink);
        linkSearchGeneratedLinkInfo.setSiteInfoType(getSiteInfoType());
        return linkSearchGeneratedLinkInfo;
    }

    private List<LinkSearchPageInfo> getLinkSearchPageInfoList(LinkSearchRequestInfo linkSearchRequestInfo, Integer totalRecordCount) {
        final List<LinkSearchPageInfo> linkSearchPageInfoList = new ArrayList<>();

        final Integer totalPageCount = (int) Math.ceil(totalRecordCount.floatValue() / getPerPageRecordCount().floatValue());
        final Integer leftPageCount = totalPageCount % SLICE_COUNT;

        final Integer fullPageCount = totalPageCount - leftPageCount;

        for (int i = 0; i < fullPageCount / SLICE_COUNT; i++) {

            final int startPage = SLICE_COUNT * i;
            final int endPage = startPage + SLICE_COUNT;

            final LinkSearchPageInfo linkSearchPageInfo = getLinkSearchPageInfo(linkSearchRequestInfo, startPage, endPage);
            linkSearchPageInfoList.add(linkSearchPageInfo);
        }

        if (totalPageCount != 0) {
            final LinkSearchPageInfo linkSearchPageInfo = getLinkSearchPageInfo(linkSearchRequestInfo, fullPageCount, totalPageCount);
            linkSearchPageInfoList.add(linkSearchPageInfo);
        }
        return linkSearchPageInfoList;
    }

    private List<QueueMessageDto> convertEntitiesToMessages(final List<LinkSearchPageInfo> linkSearchPageInfoList) {

        return linkSearchPageInfoList.stream().map(linkSearchPageInfo -> new QueueMessageDto(linkSearchPageInfo.getId())).collect(Collectors.toList());
    }

    private LinkSearchPageInfo getLinkSearchPageInfo(LinkSearchRequestInfo linkSearchRequestInfo, int startPage, int endPage) {
        final LinkSearchPageInfo linkSearchPageInfo = new LinkSearchPageInfo();
        linkSearchPageInfo.setStartPage(startPage);
        linkSearchPageInfo.setEndPage(endPage);
        linkSearchPageInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
        return linkSearchPageInfo;
    }
}