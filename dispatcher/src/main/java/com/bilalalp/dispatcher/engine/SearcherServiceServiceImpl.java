package com.bilalalp.dispatcher.engine;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.service.LinkSearchRequestInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearcherServiceServiceImpl implements SearcherService {

    @Autowired
    private LinkSearchRequestInfoService linkSearchRequestInfoService;

    @Autowired
    private SearchEngine patentScopeSearchEngine;

    @Override
    @Transactional
    public void search(final Long id) {
        final LinkSearchRequestInfo linkSearchRequestInfo = linkSearchRequestInfoService.find(id);
        patentScopeSearchEngine.crawlLink(linkSearchRequestInfo);
    }
}