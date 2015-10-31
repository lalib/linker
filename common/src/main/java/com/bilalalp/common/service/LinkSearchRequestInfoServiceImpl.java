package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.repository.LinkSearchRequestInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class LinkSearchRequestInfoServiceImpl extends AbstractService<LinkSearchRequestInfo> implements LinkSearchRequestInfoService {

    @Autowired
    private LinkSearchRequestInfoRepository linkSearchRequestInfoRepository;

    @Override
    protected CrudRepository<LinkSearchRequestInfo, Long> getRepository() {
        return linkSearchRequestInfoRepository;
    }
}