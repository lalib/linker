package com.bilalalp.dispatcher.webservice;

import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import com.bilalalp.dispatcher.dto.LinkSearchResponse;
import com.bilalalp.dispatcher.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DispatcherWebServiceImpl implements DispatcherWebService {

    @Autowired
    private DispatcherService dispatcherService;

    @Override
    public LinkSearchResponse linkSearch(final LinkSearchRequest linkSearchRequest) {

        dispatcherService.processLinkSearchRequest(linkSearchRequest);
        final LinkSearchResponse linkSearchResponse = new LinkSearchResponse();
        linkSearchResponse.setMessage("hi!");
        return linkSearchResponse;
    }

    @Override
    public String state() {
        return "App is up!";
    }
}