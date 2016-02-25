package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import com.bilalalp.common.repository.WordSummaryInfoCustomRepository;
import com.bilalalp.common.repository.WordSummaryInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WordSummaryInfoServiceImpl extends AbstractService<WordSummaryInfo> implements WordSummaryInfoService {

    @Autowired
    private WordSummaryInfoCustomRepository wordSummaryInfoCustomRepository;

    @Autowired
    private WordSummaryInfoRepository wordSummaryInfoRepository;

    @Override
    protected CrudRepository<WordSummaryInfo, Long> getRepository() {
        return wordSummaryInfoRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Long getCountByLinkSearchRequestInfoAndThresholdValue(final LinkSearchRequestInfo linkSearchRequestInfo, final Long thresholdValue) {
        return wordSummaryInfoRepository.getCountByLinkSearchRequestInfoAndThresholdValue(linkSearchRequestInfo, thresholdValue);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<WordSummaryInfo> findByLinkSearchRequestInfoAndThresholdValue(final LinkSearchRequestInfo linkSearchRequestInfo, final Long thresholdValue, final Pageable pageable) {
        return wordSummaryInfoRepository.findByLinkSearchRequestInfoAndThresholdValue(linkSearchRequestInfo, thresholdValue, pageable);
    }

    @Override
    public void bulkInsert(final LinkSearchRequestInfo linkSearchRequestInfo) {
        wordSummaryInfoCustomRepository.bulkInsert(linkSearchRequestInfo);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<WordSummaryInfo> findByLinkSearchRequestInfo(final LinkSearchRequestInfo linkSearchRequestInfo, final Pageable pageable) {
        return wordSummaryInfoRepository.findByLinkSearchRequestInfo(linkSearchRequestInfo, pageable);
    }

    @Override
    public WordSummaryInfo findByLinkSearchRequestInfoAndWord(final LinkSearchRequestInfo linkSearchRequestInfo, final String word) {
        return wordSummaryInfoRepository.findByLinkSearchRequestInfoAndWord(linkSearchRequestInfo, word);
    }
}
