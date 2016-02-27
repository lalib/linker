package com.bilalalp.wordeliminator.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.WordEliminationProcessInfo;
import com.bilalalp.common.entity.tfidf.WordEliminationRequestInfo;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.service.WordEliminationProcessInfoService;
import com.bilalalp.common.service.WordEliminationRequestInfoService;
import com.bilalalp.common.service.WordSummaryInfoService;
import com.bilalalp.wordeliminator.amqp.MessageSender;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WordEliminatorProcessor implements MessageListener {

    @Qualifier("werpQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Autowired
    private WordEliminationProcessInfoService wordEliminationProcessInfoService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private WordEliminationRequestInfoService wordEliminationRequestInfoService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private PatentInfoService patentInfoService;

    @Transactional
    public void process(final Long id) {

        final WordEliminationRequestInfo wordEliminationRequestInfo = wordEliminationRequestInfoService.find(id);
        final LinkSearchRequestInfo linkSearchRequestInfo = wordEliminationRequestInfo.getLinkSearchRequestInfo();
        final Long thresholdValue = wordEliminationRequestInfo.getThresholdValue();
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

            wordSummaryInfoList.stream()
                    .forEach(k -> applicationContext.getBean(WordEliminatorProcessor.class)
                            .saveWithNewTransaction(wordEliminationRequestInfo, k, patentInfoCount));
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveWithNewTransaction(final WordEliminationRequestInfo wordEliminationRequestInfo, final WordSummaryInfo wordSummaryInfo, final Long patentCount) {
        final WordEliminationProcessInfo wordEliminationProcessInfo = new WordEliminationProcessInfo();
        wordEliminationProcessInfo.setLinkSearchRequestInfo(wordEliminationRequestInfo.getLinkSearchRequestInfo());
        wordEliminationProcessInfo.setThresholdValue(wordEliminationRequestInfo.getThresholdValue());
        wordEliminationProcessInfo.setWordSummaryInfo(wordSummaryInfo);
        wordEliminationProcessInfo.setPatentCount(patentCount);
        wordEliminationProcessInfoService.save(wordEliminationProcessInfo);
        messageSender.sendMessage(queueConfigurationDto, new QueueMessageDto(wordEliminationProcessInfo.getId()));
    }

    @Transactional
    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        process(queueMessageDto.getId());
    }
}