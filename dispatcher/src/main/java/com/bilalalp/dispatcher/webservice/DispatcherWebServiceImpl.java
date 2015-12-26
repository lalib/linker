package com.bilalalp.dispatcher.webservice;

import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import com.bilalalp.dispatcher.dto.LinkSearchResponse;
import com.bilalalp.dispatcher.dto.StopWordCreateRequest;
import com.bilalalp.dispatcher.dto.StopWordCreateResponse;
import com.bilalalp.dispatcher.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String state() {
        return "App is up!";
    }
}