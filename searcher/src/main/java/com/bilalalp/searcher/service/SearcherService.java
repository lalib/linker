package com.bilalalp.searcher.service;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.site.SiteInfoType;

public interface SearcherService {

    void search(LinkSearchPageInfo linkSearchPageInfo);

    SiteInfoType getSiteInfoType();
}
