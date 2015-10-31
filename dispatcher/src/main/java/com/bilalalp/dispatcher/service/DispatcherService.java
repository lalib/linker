package com.bilalalp.dispatcher.service;

import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import com.bilalalp.dispatcher.dto.LinkSearchResponse;

public interface DispatcherService {

    LinkSearchResponse processLinkSearchRequest(LinkSearchRequest linkSearchRequest);
}
