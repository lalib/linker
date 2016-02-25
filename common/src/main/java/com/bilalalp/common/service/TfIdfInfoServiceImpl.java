package com.bilalalp.common.service;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.entity.tfidf.TfIdfInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.entity.tfidf.WordElimination;
import com.bilalalp.common.repository.TfIdfInfoRepository;
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
public class TfIdfInfoServiceImpl extends AbstractService<TfIdfInfo> implements TfIdfInfoService {

    @Autowired
    private TfIdfInfoRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private LinkSearchRequestInfoService linkSearchRequestInfoService;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private WordEliminationService wordEliminationService;

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Transactional
    @Override
    public void process(final Long lsrId, final Long thresholdValue) {

        final LinkSearchRequestInfo linkSearchRequestInfo = linkSearchRequestInfoService.find(lsrId);

        Long start = 0L;
        Long pageSize = 1000L;

        Long thValue = 0L;

        boolean result = true;

        while (result) {

            final Pageable pageable = new PageRequest(start.intValue(), pageSize.intValue());
            final List<WordElimination> wordEliminationList = wordEliminationService.getEliminatedWordsByLinkSearchRequestInfoAndThresholdValue(linkSearchRequestInfo, pageable);

            start += 1L;

            if (CollectionUtils.isEmpty(wordEliminationList)) {
                break;
            }

            for (final WordElimination wordElimination : wordEliminationList) {

                processEliminatedWord(linkSearchRequestInfo, wordElimination, thresholdValue);
                thValue++;
                System.out.println("TH VALUE : " + thValue);

                if (thValue.compareTo(thresholdValue) == 0) {
                    result = false;
                    break;
                }
            }
        }
    }

    private void processEliminatedWord(final LinkSearchRequestInfo linkSearchRequestInfo, final WordElimination wordElimination, final Long thresholdValue) {

        final List<PatentWordCountDto> patentWordCountDtoList = splitWordInfoService.getPatentWordCount(linkSearchRequestInfo, wordElimination.getWord());
        for (final PatentWordCountDto patentWordCountDto : patentWordCountDtoList) {
            applicationContext.getBean(TfIdfInfoServiceImpl.class).saveWithNewTransaction(wordElimination, linkSearchRequestInfo, patentWordCountDto, thresholdValue);
        }

        final List<EntityDto> patentInfoIds = patentInfoService.getPatentInfos(linkSearchRequestInfo.getId(), wordElimination.getWord());
        applicationContext.getBean(TfIdfInfoServiceImpl.class).saveWithNewTransaction(wordElimination, linkSearchRequestInfo, patentInfoIds, thresholdValue);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveWithNewTransaction(final WordElimination wordElimination, final LinkSearchRequestInfo linkSearchRequestInfo, final PatentWordCountDto patentWordCountDto, final Long thresholdValue) {

        final PatentInfo patentInfo = new PatentInfo();
        patentInfo.setId(patentWordCountDto.getPatentId());
        patentInfo.setVersion(patentWordCountDto.getPatentVersion());

        final Double tfIdfValue = patentWordCountDto.getWordCount() * wordElimination.getLogValue();

        final TfIdfInfo tfIdfInfo = new TfIdfInfo();
        tfIdfInfo.setPatentInfo(patentInfo);
        tfIdfInfo.setCount(patentWordCountDto.getWordCount());
        tfIdfInfo.setDfValue(wordElimination.getDfValue());
        tfIdfInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
        tfIdfInfo.setLogValue(wordElimination.getLogValue());
        tfIdfInfo.setPatentCount(wordElimination.getPatentCount());
        tfIdfInfo.setScore(tfIdfValue);
        tfIdfInfo.setTfValue(patentWordCountDto.getWordCount());
        tfIdfInfo.setThresholdValue(thresholdValue);
        tfIdfInfo.setWord(wordElimination.getWord());
        save(tfIdfInfo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveWithNewTransaction(final WordElimination wordElimination, final LinkSearchRequestInfo linkSearchRequestInfo, final List<EntityDto> patentInfos, final Long thresholdValue) {

        for (final EntityDto entityDto : patentInfos) {
            final PatentInfo patentInfo = new PatentInfo();
            patentInfo.setId(entityDto.getId());
            patentInfo.setVersion(entityDto.getVersion());

            final TfIdfInfo tfIdfInfo = new TfIdfInfo();
            tfIdfInfo.setPatentInfo(patentInfo);
            tfIdfInfo.setDfValue(wordElimination.getDfValue());
            tfIdfInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
            tfIdfInfo.setLogValue(wordElimination.getLogValue());
            tfIdfInfo.setPatentCount(wordElimination.getPatentCount());
            tfIdfInfo.setThresholdValue(thresholdValue);
            tfIdfInfo.setWord(wordElimination.getWord());

            tfIdfInfo.setScore(0d);
            tfIdfInfo.setCount(0L);
            tfIdfInfo.setTfValue(0L);

            save(tfIdfInfo);
        }
    }
}