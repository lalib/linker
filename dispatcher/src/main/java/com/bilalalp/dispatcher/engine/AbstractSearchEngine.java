package com.bilalalp.dispatcher.engine;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.service.LinkSearchPageInfoService;
import com.bilalalp.dispatcher.amqp.MessageSender;
import com.bilalalp.dispatcher.util.JSoapUtil;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSearchEngine {

    private static final Integer SLICE_PAGE = 50;

    @Autowired
    private LinkSearchPageInfoService linkSearchPageInfoService;

    @Autowired
    private MessageSender messageSender;

    @Qualifier(value = "linkSearcherQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    protected abstract String generateLink(final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList);

    protected abstract Integer getPageCount(Element element);

    @Transactional
    public void crawlLink(final LinkSearchRequestInfo linkSearchRequestInfo) {

        final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList = linkSearchRequestInfo.getLinkSearchRequestKeywordInfoList();

        final String generatedLink = generateLink((linkSearchRequestKeywordInfoList));

        final Element body = JSoapUtil.getBody(generatedLink);
        final Integer totalPageCount = getPageCount(body);

        System.out.println(totalPageCount);
        final List<LinkSearchPageInfo> linkSearchPageInfoList = getLinkSearchPageInfoList(linkSearchRequestInfo, totalPageCount);

        linkSearchPageInfoService.save(linkSearchPageInfoList);

        final List<QueueMessageDto> queueMessageDtoList = convertEntitiesToMessages(linkSearchPageInfoList);
        messageSender.sendMessage(queueConfigurationDto, queueMessageDtoList);
    }

    private List<LinkSearchPageInfo> getLinkSearchPageInfoList(LinkSearchRequestInfo linkSearchRequestInfo, Integer totalPageCount) {
        final List<LinkSearchPageInfo> linkSearchPageInfoList = new ArrayList<>();

        final Integer leftPageCount = totalPageCount % SLICE_PAGE;
        final Integer fullPageCount = totalPageCount - leftPageCount;

        for (int i = 0; i < fullPageCount / SLICE_PAGE; i++) {

            final int startPage = SLICE_PAGE * i;
            final int endPage = startPage + SLICE_PAGE;

            final LinkSearchPageInfo linkSearchPageInfo = getLinkSearchPageInfo(linkSearchRequestInfo, startPage, endPage);
            linkSearchPageInfoList.add(linkSearchPageInfo);
        }

        if (leftPageCount != 0) {
            final LinkSearchPageInfo linkSearchPageInfo = getLinkSearchPageInfo(linkSearchRequestInfo, fullPageCount, leftPageCount);
            linkSearchPageInfoList.add(linkSearchPageInfo);
        }
        return linkSearchPageInfoList;
    }

    private List<QueueMessageDto> convertEntitiesToMessages(final List<LinkSearchPageInfo> linkSearchPageInfoList) {

        final List<QueueMessageDto> queueMessageDtoList = new ArrayList<>();

        for (final LinkSearchPageInfo linkSearchPageInfo : linkSearchPageInfoList) {
            queueMessageDtoList.add(new QueueMessageDto(linkSearchPageInfo.getId()));
        }

        return queueMessageDtoList;
    }

    private LinkSearchPageInfo getLinkSearchPageInfo(LinkSearchRequestInfo linkSearchRequestInfo, int startPage, int endPage) {
        final LinkSearchPageInfo linkSearchPageInfo = new LinkSearchPageInfo();
        linkSearchPageInfo.setStartPage(startPage);
        linkSearchPageInfo.setEndPage(endPage);
        linkSearchPageInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
        return linkSearchPageInfo;
    }

}