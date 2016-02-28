package com.bilalalp.wordprocessor.service;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.WordElimination;
import com.bilalalp.common.entity.tfidf.WordEliminationProcessInfo;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import com.bilalalp.common.service.SplitWordInfoService;
import com.bilalalp.common.service.WordEliminationProcessInfoService;
import com.bilalalp.common.service.WordEliminationService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordProcessorService implements MessageListener {

    @Autowired
    private WordEliminationProcessInfoService wordEliminationProcessInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private WordEliminationService wordEliminationService;

    @Transactional
    public void process(final Long id) {

        final WordEliminationProcessInfo wordEliminationProcessInfo = wordEliminationProcessInfoService.find(id);
        final LinkSearchRequestInfo linkSearchRequestInfo = wordEliminationProcessInfo.getLinkSearchRequestInfo();
        final WordSummaryInfo wordSummaryInfo = wordEliminationProcessInfo.getWordSummaryInfo();
        final Long patentCount = wordEliminationProcessInfo.getPatentCount();

        final Long dfValue = splitWordInfoService.getWordCountByLinkSearchRequestInfoAndWord(linkSearchRequestInfo, wordSummaryInfo.getWord());
        final double logResult = Math.log(wordSummaryInfo.getCount().doubleValue() / dfValue);
        final Double tfIdfResult = dfValue.doubleValue() * logResult;

        final WordElimination wordElimination = new WordElimination();
        wordElimination.setLinkSearchRequestInfo(linkSearchRequestInfo);
        wordElimination.setScore(tfIdfResult);
        wordElimination.setThresholdValue(wordEliminationProcessInfo.getThresholdValue());
        wordElimination.setWord(wordSummaryInfo.getWord());
        wordElimination.setCount(wordSummaryInfo.getCount());
        wordElimination.setDfValue(dfValue);
        wordElimination.setLogValue(logResult);
        wordElimination.setPatentCount(patentCount);
        wordElimination.setTfValue(wordSummaryInfo.getCount());
        wordEliminationService.save(wordElimination);
    }

    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println(queueMessageDto.getId());
        process(queueMessageDto.getId());
    }
}