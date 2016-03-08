package com.bilalalp.common.service;


import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.WordElimination;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import com.bilalalp.common.repository.WordEliminationRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Service
public class WordEliminationServiceImpl extends AbstractService<WordElimination> implements WordEliminationService {

    @Autowired
    private WordEliminationRepository repository;

    @Autowired
    private LinkSearchRequestInfoService linkSearchRequestInfoService;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private ApplicationContext applicationContext;

    @Transactional
    @Override
    public void process(final Long lsrId, final Long thresholdValue) {

        final LinkSearchRequestInfo linkSearchRequestInfo = linkSearchRequestInfoService.find(lsrId);
        final Long patentInfoCount = patentInfoService.getPatentInfoCountByLinkSearchPageInfo(linkSearchRequestInfo);

        Long start = 0L;
        Long pageSize = 1000L;

        while (true) {

            final Pageable pageable = new PageRequest(start.intValue(), pageSize.intValue());
            final List<WordSummaryInfo> wordSummaryInfoList = wordSummaryInfoService.findByLinkSearchRequestInfoAndThresholdValue(linkSearchRequestInfo, thresholdValue, pageable);
            start += 1L;

            if (CollectionUtils.isEmpty(wordSummaryInfoList)) {
                break;
            }

            for (final WordSummaryInfo wordSummaryInfo : wordSummaryInfoList) {

                final Long dfValue = splitWordInfoService.getWordCountByLinkSearchRequestInfoAndWord(linkSearchRequestInfo, wordSummaryInfo.getId());
                final Long numberOfPatentCount = splitWordInfoService.getSplitWordCount(linkSearchRequestInfo, wordSummaryInfo.getId());
                final double logResult = Math.log(patentInfoCount.doubleValue() / numberOfPatentCount);
                final Double tfIdfResult = dfValue.doubleValue() * logResult;

                final WordElimination wordElimination = new WordElimination();
                wordElimination.setLinkSearchRequestInfo(linkSearchRequestInfo);
                wordElimination.setScore(tfIdfResult);
                wordElimination.setThresholdValue(thresholdValue);
                wordElimination.setWordInfoId(wordSummaryInfo.getId());
                wordElimination.setCount(wordSummaryInfo.getCount());
                wordElimination.setDfValue(dfValue);
                wordElimination.setLogValue(logResult);
                wordElimination.setPatentCount(patentInfoCount);
                wordElimination.setTfValue(numberOfPatentCount);

                final WordEliminationService wordEliminationService = applicationContext.getBean(WordEliminationService.class);
                wordEliminationService.saveWithNewTransaction(wordElimination);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveWithNewTransaction(final WordElimination wordElimination) {
        save(wordElimination);
    }

    @Override
    public List<WordElimination> getEliminatedWordsByLinkSearchRequestInfoAndThresholdValue(final LinkSearchRequestInfo linkSearchRequestInfo, final Pageable pageable) {
        return repository.getEliminatedWordsByLinkSearchRequestInfoAndThresholdValue(linkSearchRequestInfo, pageable);
    }
}