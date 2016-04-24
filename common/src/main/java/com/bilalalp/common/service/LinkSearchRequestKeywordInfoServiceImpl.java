package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import com.bilalalp.common.repository.LinkSearchRequestKeywordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Getter
@Service
public class LinkSearchRequestKeywordInfoServiceImpl extends AbstractService<LinkSearchRequestKeywordInfo> implements LinkSearchRequestKeywordInfoService {

    @Autowired
    private LinkSearchRequestKeywordInfoRepository repository;
}