package com.bilalalp.dispatcher.service;

import com.bilalalp.dispatcher.dto.*;

public interface DispatcherService {

    LinkSearchResponse processLinkSearchRequest(LinkSearchRequest linkSearchRequest);

    StopWordCreateResponse processCreateStopWordRequest(StopWordCreateRequest stopWordCreateRequest);

    WordSummaryCreateResponse processCreateWordSummary(WordSummaryCreateRequest wordSummaryCreateRequest);

    KeywordSelectionResponseDto processSelectKeywordRequest(KeywordSelectionRequestDto keywordSelectionRequestDto);

    void eliminate(Long lsrId, Long threshold);

    void calculateTfIdf(Long lsrId, Long thresholdValue);

    void generateCluster(Long clusterNumber, Long tfIdfRequestId, String clusterType);

    void analyzeCluster(Long clusterId, Long wordLimit);

    void calculateTv();

    void completeTvCalculation();

    void createRandomPatentList(Long patentCount);
}