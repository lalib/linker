package com.bilalalp.gatherer.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestSiteInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.LinkSearchRequestInfoService;
import com.bilalalp.gatherer.engine.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class SearcherServiceServiceImpl implements SearcherService {

    private static final Map<SiteInfoType, SearchEngine> SEARCH_ENGINE_MAP = new EnumMap<>(SiteInfoType.class);
    @Autowired
    private LinkSearchRequestInfoService linkSearchRequestInfoService;
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        final Collection<SearchEngine> values = applicationContext.getBeansOfType(SearchEngine.class).values();
        for (final SearchEngine searchEngine : values) {
            SEARCH_ENGINE_MAP.put(searchEngine.getSiteInfoType(), searchEngine);
        }
    }

    @Override
    @Transactional
    public void search(final Long id) {
        final LinkSearchRequestInfo linkSearchRequestInfo = linkSearchRequestInfoService.find(id);

        final List<LinkSearchRequestSiteInfo> linkSearchRequestSiteInfoList = linkSearchRequestInfo.getLinkSearchRequestSiteInfoList();

        for (final LinkSearchRequestSiteInfo linkSearchRequestSiteInfo : linkSearchRequestSiteInfoList) {
            SEARCH_ENGINE_MAP.get(linkSearchRequestSiteInfo.getSiteInfo().getSiteInfoType()).crawlLink(linkSearchRequestInfo);
        }
    }
}