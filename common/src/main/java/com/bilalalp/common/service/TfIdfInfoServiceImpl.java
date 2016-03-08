package com.bilalalp.common.service;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.entity.tfidf.*;
import com.bilalalp.common.repository.TfIdfInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveWithNewTransaction(final WordElimination wordElimination, final LinkSearchRequestInfo linkSearchRequestInfo, final PatentWordCountDto patentWordCountDto, final Long thresholdValue) {

        final PatentInfo patentInfo = new PatentInfo();
        patentInfo.setId(patentWordCountDto.getPatentId());
        patentInfo.setVersion(patentWordCountDto.getPatentVersion());

        final Double tfIdfValue = patentWordCountDto.getWordCount() * wordElimination.getLogValue();

        final TfIdfInfo tfIdfInfo = new TfIdfInfo();
        tfIdfInfo.setPatentInfoId(patentInfo.getId());
        tfIdfInfo.setCount(patentWordCountDto.getWordCount());
        tfIdfInfo.setDfValue(wordElimination.getDfValue());
        tfIdfInfo.setLinkSearchRequestInfoId(linkSearchRequestInfo.getId());
        tfIdfInfo.setLogValue(wordElimination.getLogValue());
        tfIdfInfo.setPatentCount(wordElimination.getPatentCount());
        tfIdfInfo.setScore(tfIdfValue);
        tfIdfInfo.setTfValue(patentWordCountDto.getWordCount());
        tfIdfInfo.setThresholdValue(thresholdValue);
        tfIdfInfo.setWordInfoId(wordElimination.getWordInfoId());
        save(tfIdfInfo);
    }

    @Override
    public void processEliminatedWord(final TfIdfProcessInfo tfIdfProcessInfo) {

        final LinkSearchRequestInfo linkSearchRequestInfo = tfIdfProcessInfo.getLinkSearchRequestInfo();
        final Long thresholdValue = tfIdfProcessInfo.getThresholdValue();
        final WordElimination wordElimination = tfIdfProcessInfo.getWordElimination();
        final List<PatentWordCountDto> patentWordCountDtoList = splitWordInfoService.getPatentWordCount(linkSearchRequestInfo, wordElimination.getWordInfoId());

        for (final PatentWordCountDto patentWordCountDto : patentWordCountDtoList) {
            applicationContext.getBean(TfIdfInfoServiceImpl.class).saveWithNewTransaction(wordElimination, linkSearchRequestInfo, patentWordCountDto, thresholdValue);
        }

        final List<EntityDto> patentInfoIds = patentInfoService.getPatentInfos(linkSearchRequestInfo.getId(), wordElimination.getWordInfoId());
        applicationContext.getBean(TfIdfInfoServiceImpl.class).saveWithNewTransaction(tfIdfProcessInfo, patentInfoIds);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveWithNewTransaction(final TfIdfProcessInfo tfIdfProcessInfo, final List<EntityDto> patentInfoIds) {

        final WordElimination wordElimination = tfIdfProcessInfo.getWordElimination();
        final LinkSearchRequestInfo linkSearchRequestInfo = tfIdfProcessInfo.getLinkSearchRequestInfo();
        final Long thresholdValue = tfIdfProcessInfo.getThresholdValue();
        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfProcessInfo.getTfIdfRequestInfo();

        for (final EntityDto entityDto : patentInfoIds) {
            final PatentInfo patentInfo = new PatentInfo();
            patentInfo.setId(entityDto.getId());
            patentInfo.setVersion(entityDto.getVersion());

            final TfIdfInfo tfIdfInfo = new TfIdfInfo();
            tfIdfInfo.setPatentInfoId(patentInfo.getId());
            tfIdfInfo.setDfValue(wordElimination.getDfValue());
            tfIdfInfo.setLinkSearchRequestInfoId(linkSearchRequestInfo.getId());
            tfIdfInfo.setLogValue(wordElimination.getLogValue());
            tfIdfInfo.setPatentCount(wordElimination.getPatentCount());
            tfIdfInfo.setThresholdValue(thresholdValue);
            tfIdfInfo.setWordInfoId(wordElimination.getWordInfoId());
            tfIdfInfo.setTfIdfRequestInfoId(tfIdfRequestInfo.getId());

            tfIdfInfo.setScore(0d);
            tfIdfInfo.setCount(0L);
            tfIdfInfo.setTfValue(0L);

            save(tfIdfInfo);
        }
    }
}