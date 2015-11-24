package com.bilalalp.searcher.service;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import org.springframework.stereotype.Service;

@Service
public class UsptoSearcherService implements SearcherService {

    @Override
    public void search(final LinkSearchPageInfo linkSearchPageInfo) {

    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.USPTO;
    }
}