package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchGeneratedLinkInfo;
import com.bilalalp.common.repository.LinkSearchGeneratedLinkInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class LinkSearchGeneratedLinkInfoServiceImpl extends AbstractService<LinkSearchGeneratedLinkInfo> implements LinkSearchGeneratedLinkInfoService {

    @Autowired
    private LinkSearchGeneratedLinkInfoRepository linkSearchGeneratedLinkInfoRepository;

    @Override
    protected CrudRepository<LinkSearchGeneratedLinkInfo, Long> getRepository() {
        return linkSearchGeneratedLinkInfoRepository;
    }
}