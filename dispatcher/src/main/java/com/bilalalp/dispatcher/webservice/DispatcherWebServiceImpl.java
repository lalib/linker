package com.bilalalp.dispatcher.webservice;

import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import com.bilalalp.dispatcher.dto.LinkSearchResponse;

public class DispatcherWebServiceImpl implements DispatcherWebService {

    @Override
    public LinkSearchResponse linkSearch(final LinkSearchRequest linkSearchRequest) {

        final LinkSearchResponse linkSearchResponse = new LinkSearchResponse();
        linkSearchResponse.setMessage("hi!");
        return linkSearchResponse;
    }

    @Override
    public String state() {
        return "App is up!";
    }
}