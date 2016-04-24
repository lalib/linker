package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.repository.LinkSearchPageInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class LinkSearchPageInfoServiceImpl extends AbstractService<LinkSearchPageInfo> implements LinkSearchPageInfoService {

    @Autowired
    private LinkSearchPageInfoRepository repository;

    @Override
    public List<LinkSearchPageInfo> getLinkSearchPageInfoListBylinkSearchRequestInfo(final LinkSearchRequestInfo linkSearchRequestInfo) {
        return repository.getLinkSearchPageInfoListBylinkSearchRequestInfo(linkSearchRequestInfo);
    }
}