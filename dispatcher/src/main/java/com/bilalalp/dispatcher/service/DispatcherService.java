package com.bilalalp.dispatcher.service;

import com.bilalalp.dispatcher.dto.*;

public interface DispatcherService {

    LinkSearchResponse processLinkSearchRequest(LinkSearchRequest linkSearchRequest);

    StopWordCreateResponse processCreateStopWordRequest(StopWordCreateRequest stopWordCreateRequest);

    WordSummaryCreateResponse processCreateWordSummary(WordSummaryCreateRequest wordSummaryCreateRequest);
}