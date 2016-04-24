package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestSiteInfo;
import com.bilalalp.common.repository.LinkSearchRequestSiteInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Getter
@Service
public class LinkSearchRequestSiteInfoServiceImpl extends AbstractService<LinkSearchRequestSiteInfo> implements LinkSearchRequestSiteInfoService {

    @Autowired
    private LinkSearchRequestSiteInfoRepository repository;
}