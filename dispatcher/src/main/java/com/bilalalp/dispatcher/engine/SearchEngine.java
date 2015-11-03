package com.bilalalp.dispatcher.engine;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;

public interface SearchEngine {

    void crawlLink(final LinkSearchRequestInfo linkSearchRequestInfo);
}