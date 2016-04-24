package com.bilalalp.common.service;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.repository.PatentInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Service
public class PatentInfoServiceImpl extends AbstractService<PatentInfo> implements PatentInfoService {

    @Autowired
    private PatentInfoRepository repository;

    @Override
    public List<PatentInfo> getPatentListBylinkSearchPageInfo(final LinkSearchPageInfo linkSearchPageInfo) {
        return repository.getPatentListBylinkSearchPageInfo(linkSearchPageInfo);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Long> getPatentIds(final Long requestId) {
        return repository.getPatentIds(requestId);
    }

    @Transactional
    @Override
    public void resetParseInformation(final Long requestId) {
        repository.resetParseInformation(requestId);
    }

    @Override
    public Long getPatentInfoCountByLinkSearchPageInfo(final LinkSearchRequestInfo linkSearchRequestInfo) {
        return repository.getPatentInfoCountByLinkSearchRequestInfo(linkSearchRequestInfo);
    }

    @Override
    public List<EntityDto> getPatentInfos(final Long lsrId, final Long word) {
        return repository.getPatentInfoIds(lsrId, word);
    }

    @Transactional
    @Override
    public List<Long> getPatentIds(final Long requestId, final String word) {
        return repository.getPatentIds(requestId, word);
    }
}