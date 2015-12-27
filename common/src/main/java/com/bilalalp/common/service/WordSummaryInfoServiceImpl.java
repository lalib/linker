package com.bilalalp.common.service;

import com.bilalalp.common.entity.WordSummaryInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.repository.WordSummaryInfoCustomRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class WordSummaryInfoServiceImpl extends AbstractService<WordSummaryInfo> implements WordSummaryInfoService {

    @Autowired
    private WordSummaryInfoCustomRepository wordSummaryInfoCustomRepository;

    @Override
    protected CrudRepository<WordSummaryInfo, Long> getRepository() {
        return null;
    }

    @Override
    public void bulkInsert(final LinkSearchRequestInfo linkSearchRequestInfo) {
        wordSummaryInfoCustomRepository.bulkInsert(linkSearchRequestInfo);
    }
}
