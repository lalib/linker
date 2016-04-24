package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import com.bilalalp.common.repository.WordSummaryInfoCustomRepository;
import com.bilalalp.common.repository.WordSummaryInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Service
public class WordSummaryInfoServiceImpl extends AbstractService<WordSummaryInfo> implements WordSummaryInfoService {

    @Autowired
    private WordSummaryInfoCustomRepository wordSummaryInfoCustomRepository;

    @Autowired
    private WordSummaryInfoRepository repository;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Long getCountByLinkSearchRequestInfoAndThresholdValue(final LinkSearchRequestInfo linkSearchRequestInfo, final Long thresholdValue) {
        return repository.getCountByLinkSearchRequestInfoAndThresholdValue(linkSearchRequestInfo, thresholdValue);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<WordSummaryInfo> findByLinkSearchRequestInfoAndThresholdValue(final LinkSearchRequestInfo linkSearchRequestInfo, final Long thresholdValue, final Pageable pageable) {
        return repository.findByLinkSearchRequestInfoAndThresholdValue(linkSearchRequestInfo, thresholdValue, pageable);
    }

    @Override
    public void bulkInsert(final LinkSearchRequestInfo linkSearchRequestInfo) {
        wordSummaryInfoCustomRepository.bulkInsert(linkSearchRequestInfo);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<WordSummaryInfo> findByLinkSearchRequestInfo(final LinkSearchRequestInfo linkSearchRequestInfo, final Pageable pageable) {
        return repository.findByLinkSearchRequestInfo(linkSearchRequestInfo, pageable);
    }

    @Override
    public WordSummaryInfo findByLinkSearchRequestInfoAndWord(final LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId) {
        return repository.findByLinkSearchRequestInfoAndWord(linkSearchRequestInfo, wordInfoId);
    }

    @Override
    public List<Long> getWordIds(final Long lsrId, final List<String> wordList) {
        return wordSummaryInfoCustomRepository.getWordIds(lsrId, wordList);
    }

    @Override
    public List<BigDecimal> getTvWordIds() {
        return wordSummaryInfoCustomRepository.getTvWordIds();
    }

    @Override
    public List<TvProcessInfo> getTfResult(Long patentId, Long lsrId, Long patentCount){
        return wordSummaryInfoCustomRepository.getTfResult(patentId,lsrId, patentCount);
    }
}
