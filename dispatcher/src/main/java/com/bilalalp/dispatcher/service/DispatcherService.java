package com.bilalalp.dispatcher.service;

import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import com.bilalalp.dispatcher.dto.LinkSearchResponse;
import com.bilalalp.dispatcher.dto.StopWordCreateRequest;
import com.bilalalp.dispatcher.dto.StopWordCreateResponse;

public interface DispatcherService {

    LinkSearchResponse processLinkSearchRequest(LinkSearchRequest linkSearchRequest);

    StopWordCreateResponse processCreateStopWordRequest(StopWordCreateRequest stopWordCreateRequest);
}
