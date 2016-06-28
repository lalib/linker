package com.bilalalp.dispatcher.webservice;

import com.bilalalp.dispatcher.dto.*;
import com.bilalalp.dispatcher.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
public class DispatcherWebServiceImpl implements DispatcherWebService {

    @Autowired
    private DispatcherService dispatcherService;

    @Override
    public LinkSearchResponse linkSearch(final LinkSearchRequest linkSearchRequest) {
        return dispatcherService.processLinkSearchRequest(linkSearchRequest);
    }

    @Override
    public StopWordCreateResponse createStopWord(final StopWordCreateRequest stopWordCreateRequest) {
        return dispatcherService.processCreateStopWordRequest(stopWordCreateRequest);
    }

    @Override
    public WordSummaryCreateResponse createWordSummary(final WordSummaryCreateRequest wordSummaryCreateRequest) {
        return dispatcherService.processCreateWordSummary(wordSummaryCreateRequest);
    }

    @Override
    public KeywordSelectionResponseDto selectKeyword(final KeywordSelectionRequestDto keywordSelectionRequestDto) {
        return dispatcherService.processSelectKeywordRequest(keywordSelectionRequestDto);
    }

    @Override
    public Response eliminateKeywords(final Long lsrId, final Long threshold) {
        dispatcherService.eliminate(lsrId, threshold);
        return Response.ok().build();
    }

    @Override
    public Response calculateTfIdf(final Long lsrId, final Long thresholdValue) {
        dispatcherService.calculateTfIdf(lsrId, thresholdValue);
        return Response.ok().build();
    }

    @Override
    public Response generateCluster(final Long clusterNumber, final Long tfIdfRequestId, final String clusterType) {
        dispatcherService.generateCluster(clusterNumber, tfIdfRequestId, clusterType);
        return Response.ok().build();
    }

    @Override
    public Response analyzeCluster(final Long clusterId, final Long wordLimit) {
        dispatcherService.analyzeCluster(clusterId, wordLimit);
        return Response.ok().build();
    }

    @Override
    public Response completeTvCalculation() {
        dispatcherService.completeTvCalculation();
        return Response.ok().build();
    }

    @Override
    public Response calculateTv() {
        dispatcherService.calculateTv();
        return Response.ok().build();
    }

    @Override
    public String state() {
        return "App is up!";
    }

    @Override
    public Response createRandomPatentList(final Long patentCount) {
        dispatcherService.createRandomPatentList(patentCount);
        return Response.ok().build();
    }
}