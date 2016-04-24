package com.bilalalp.tfidfinitializer.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.*;
import com.bilalalp.common.service.*;
import com.bilalalp.tfidfinitializer.amqp.MessageSender;
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

import java.math.BigInteger;
import java.util.List;

@Service
public class TfIdfInitializerService implements MessageListener {

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Autowired
    private TfIdfProcessInfoService tfIdfProcessInfoService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AnalyzableWordInfoService analyzableWordInfoService;

    @Autowired
    private WordEliminationService wordEliminationService;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private MessageSender messageSender;

    @Qualifier("tfIdfProcessQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private TvProcessInfoService tvProcessInfoService;

    @Transactional
    public void process(final Long id) {

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(id);
        final LinkSearchRequestInfo linkSearchRequestInfo = tfIdfRequestInfo.getLinkSearchRequestInfo();

//        saveWords(tfIdfRequestInfo, linkSearchRequestInfo);
        saveWordsForRange(tfIdfRequestInfo);

        arrangePatents(tfIdfRequestInfo, linkSearchRequestInfo);
    }

    private void saveWordsForRange(final TfIdfRequestInfo tfIdfRequestInfo) {

        final List<TvProcessInfo> tvProcessInfoList = tvProcessInfoService.findByLimit(tfIdfRequestInfo.getThresholdValue().intValue());
//        final List<BigInteger> words = splitWordInfoService.getWords(tfIdfRequestInfo.getLinkSearchRequestInfo().getId());

        for (final TvProcessInfo tvProcessInfo : tvProcessInfoList) {
            final AnalyzableWordInfo analyzableWordInfo = new AnalyzableWordInfo();
            analyzableWordInfo.setTfIdfRequestInfo(tfIdfRequestInfo);
            analyzableWordInfo.setWordId(tvProcessInfo.getWordId());
            applicationContext.getBean(TfIdfInitializerService.class).save(analyzableWordInfo);
        }
    }

    private void arrangePatents(final TfIdfRequestInfo tfIdfRequestInfo, final LinkSearchRequestInfo linkSearchRequestInfo) {

        final List<Long> patentIds = patentInfoService.getPatentIds(linkSearchRequestInfo.getId());

        for (final Long id : patentIds) {
            final TfIdfProcessInfo tfIdfProcessInfo = new TfIdfProcessInfo();
            tfIdfProcessInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
            tfIdfProcessInfo.setPatentInfoId(id);
            tfIdfProcessInfo.setTfIdfRequestInfo(tfIdfRequestInfo);
            tfIdfProcessInfo.setThresholdValue(tfIdfRequestInfo.getThresholdValue());
            applicationContext.getBean(TfIdfInitializerService.class).saveAndSendToQueue(tfIdfProcessInfo);
        }
    }

    private void saveWords(TfIdfRequestInfo tfIdfRequestInfo, LinkSearchRequestInfo linkSearchRequestInfo) {
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

                final AnalyzableWordInfo analyzableWordInfo = new AnalyzableWordInfo();
                analyzableWordInfo.setTfIdfRequestInfo(tfIdfRequestInfo);
                analyzableWordInfo.setWordId(wordElimination.getWordInfoId());
                applicationContext.getBean(TfIdfInitializerService.class).save(analyzableWordInfo);

                thValue++;

                if (thValue.compareTo(tfIdfRequestInfo.getThresholdValue()) == 0) {
                    result = false;
                    break;
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(final AnalyzableWordInfo analyzableWordInfo) {
        analyzableWordInfoService.save(analyzableWordInfo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAndSendToQueue(final TfIdfProcessInfo tfIdfProcessInfo) {
        tfIdfProcessInfoService.save(tfIdfProcessInfo);
        messageSender.sendMessage(queueConfigurationDto, new QueueMessageDto(tfIdfProcessInfo.getId()));
    }

    @Transactional
    @Override
    public void onMessage(final Message message) {
        try {
            final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
            process(queueMessageDto.getId());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}