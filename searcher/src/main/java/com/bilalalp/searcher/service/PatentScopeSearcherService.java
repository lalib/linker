package com.bilalalp.searcher.service;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatentScopeSearcherService implements SearcherService {

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void search(final LinkSearchPageInfo linkSearchPageInfo) {

        linkSearchPageInfo.getLinkSearchRequestInfo();
    }
}