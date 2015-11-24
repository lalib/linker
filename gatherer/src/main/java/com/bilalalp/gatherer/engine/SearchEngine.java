package com.bilalalp.gatherer.engine;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.site.SiteInfoType;

public interface SearchEngine {

     SiteInfoType getSiteInfoType();

    void crawlLink(final LinkSearchRequestInfo linkSearchRequestInfo);
}