package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.repository.LinkSearchPageInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class LinkSearchPageInfoServiceImpl extends AbstractService<LinkSearchPageInfo> implements LinkSearchPageInfoService {

    @Autowired
    private LinkSearchPageInfoRepository linkSearchPageInfoRepository;

    @Override
    protected CrudRepository<LinkSearchPageInfo, Long> getRepository() {
        return linkSearchPageInfoRepository;
    }
}