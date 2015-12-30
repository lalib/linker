package com.bilalalp.dispatcher.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestSiteInfo;
import com.bilalalp.common.entity.patent.KeywordSelectionRequest;
import com.bilalalp.common.entity.patent.StopWordInfo;
import com.bilalalp.common.entity.site.SiteInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.*;
import com.bilalalp.dispatcher.amqp.MessageSender;
import com.bilalalp.dispatcher.dto.*;
import com.bilalalp.dispatcher.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DispatcherServiceImpl implements DispatcherService {

    @Autowired
    private MessageSender messageSender;

    @Qualifier(value = "dispatcherRequestQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Qualifier(value = "selectorQueueConfiguration")
    @Autowired
    private QueueConfigurationDto selectorQueueConfigurationDto;

    @Autowired
    private Validator<LinkSearchRequest> linkSearchRequestValidator;

    @Autowired
    private SiteInfoService siteInfoService;

    @Autowired
    private StopWordInfoService stopWordInfoService;

    @Autowired
    private LinkSearchRequestInfoService linkSearchRequestInfoService;

    @Autowired
    private LinkSearchRequestKeywordInfoService linkSearchRequestKeywordInfoService;

    @Autowired
    private LinkSearchRequestSiteInfoService linkSearchRequestSiteInfoService;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private KeywordSelectionRequestService keywordSelectionRequestService;

    @Override
    @Transactional
    public LinkSearchResponse processLinkSearchRequest(final LinkSearchRequest linkSearchRequest) {

        linkSearchRequestValidator.validate(linkSearchRequest);

        final Long requestId = persistRequest(linkSearchRequest);
        messageSender.sendMessage(queueConfigurationDto, new QueueMessageDto(requestId));

        return new LinkSearchResponse(requestId);
    }

    @Override
    public StopWordCreateResponse processCreateStopWordRequest(final StopWordCreateRequest stopWordCreateRequest) {

        final List<String> stopWordList = stopWordCreateRequest.getStopWordList();

        for (final String stopWord : stopWordList) {
            final StopWordInfo stopWordInfo = new StopWordInfo();
            stopWordInfo.setStopWord(stopWord);
            stopWordInfoService.save(stopWordInfo);
        }
        return new StopWordCreateResponse();
    }

    @Override
    @Transactional
    public WordSummaryCreateResponse processCreateWordSummary(final WordSummaryCreateRequest wordSummaryCreateRequest) {
        final Long linkSearchRequestInfoId = wordSummaryCreateRequest.getLinkSearchRequestInfoId();
        final LinkSearchRequestInfo linkSearchRequestInfo = linkSearchRequestInfoService.find(linkSearchRequestInfoId);
        wordSummaryInfoService.bulkInsert(linkSearchRequestInfo);
        return new WordSummaryCreateResponse();
    }

    @Override
    public KeywordSelectionResponseDto processSelectKeywordRequest(final KeywordSelectionRequestDto keywordSelectionRequestDto) {
        final KeywordSelectionRequest keywordSelectionRequest = new KeywordSelectionRequest();
        keywordSelectionRequest.setFirstRequestId(keywordSelectionRequest.getFirstRequestId());
        keywordSelectionRequest.setSecondRequestId(keywordSelectionRequest.getSecondRequestId());
        keywordSelectionRequestService.save(keywordSelectionRequest);
        messageSender.sendMessage(selectorQueueConfigurationDto, new QueueMessageDto(keywordSelectionRequest.getId()));
        return new KeywordSelectionResponseDto(keywordSelectionRequest.getId());
    }

    private Long persistRequest(final LinkSearchRequest linkSearchRequest) {

        final List<SiteInfoType> filteredInfoTypeList = filterSiteInfoTypeList(linkSearchRequest.getSiteInfoTypeList());
        final List<String> filteredKeywordList = filteredKeywordInfoList(linkSearchRequest.getKeywordList());

        final LinkSearchRequestInfo linkSearchRequestInfo = new LinkSearchRequestInfo();
        linkSearchRequestInfo.setInternationalPatentClass(linkSearchRequest.getInternationalPatentClass());
        linkSearchRequestInfo.setInternationalPatentClassSearch(linkSearchRequest.isPatentClassSearch());
        final List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList = createLinkSearchRequestKeywordInfoList(linkSearchRequestInfo, filteredKeywordList);
        final List<LinkSearchRequestSiteInfo> linkSearchRequestSiteInfoList = createLinkSearchRequestSiteInfoList(linkSearchRequestInfo, filteredInfoTypeList);

        linkSearchRequestInfoService.save(linkSearchRequestInfo);
        linkSearchRequestKeywordInfoService.save(linkSearchRequestKeywordInfoList);
        linkSearchRequestSiteInfoService.save(linkSearchRequestSiteInfoList);

        return linkSearchRequestInfo.getId();
    }

    private List<LinkSearchRequestSiteInfo> createLinkSearchRequestSiteInfoList(final LinkSearchRequestInfo linkSearchRequestInfo, final List<SiteInfoType> siteInfoTypeList) {

        return siteInfoTypeList.stream().map(k -> {

            final SiteInfo siteInfo = siteInfoService.getSiteInfoBySiteInfoType(k);

            final LinkSearchRequestSiteInfo linkSearchRequestSiteInfo = new LinkSearchRequestSiteInfo();
            linkSearchRequestSiteInfo.setSiteInfo(siteInfo);
            linkSearchRequestSiteInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
            return linkSearchRequestSiteInfo;

        }).collect(Collectors.toList());
    }

    private List<LinkSearchRequestKeywordInfo> createLinkSearchRequestKeywordInfoList(final LinkSearchRequestInfo linkSearchRequestInfo, final List<String> keywordList) {
        return keywordList.stream().map(k -> {

            final LinkSearchRequestKeywordInfo linkSearchRequestKeywordInfo = new LinkSearchRequestKeywordInfo();
            linkSearchRequestKeywordInfo.setKeyword(k);
            linkSearchRequestKeywordInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
            return linkSearchRequestKeywordInfo;

        }).collect(Collectors.toList());
    }

    private List<SiteInfoType> filterSiteInfoTypeList(final List<SiteInfoType> siteInfoTypes) {
        return new ArrayList<>(siteInfoTypes.stream().collect(Collectors.toSet()));
    }

    private List<String> filteredKeywordInfoList(final List<String> keywordList) {
        final Set<String> filteredKeywordSet = keywordList.stream().map(k -> k.trim().toLowerCase()).collect(Collectors.toSet());
        return new ArrayList<>(filteredKeywordSet);
    }
}