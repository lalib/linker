package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestSiteInfo;
import com.bilalalp.common.repository.LinkSearchRequestSiteInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class LinkSearchRequestSiteInfoServiceImpl extends AbstractService<LinkSearchRequestSiteInfo> implements LinkSearchRequestSiteInfoService {

    @Autowired
    private LinkSearchRequestSiteInfoRepository linkSearchRequestSiteInfoRepository;

    @Override
    protected CrudRepository<LinkSearchRequestSiteInfo, Long> getRepository() {
        return linkSearchRequestSiteInfoRepository;
    }
}