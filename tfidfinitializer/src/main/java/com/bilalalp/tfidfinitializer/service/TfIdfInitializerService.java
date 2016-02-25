package com.bilalalp.tfidfinitializer.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfProcessInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.entity.tfidf.WordElimination;
import com.bilalalp.common.service.TfIdfProcessInfoService;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import com.bilalalp.common.service.WordEliminationService;
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
    private WordEliminationService wordEliminationService;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private MessageSender messageSender;

    @Qualifier("tfIdfProcessQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Transactional
    public void process(final Long id) {

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(id);
        final LinkSearchRequestInfo linkSearchRequestInfo = tfIdfRequestInfo.getLinkSearchRequestInfo();

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

                thValue++;
                final TfIdfProcessInfo tfIdfProcessInfo = new TfIdfProcessInfo();
                tfIdfProcessInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
                tfIdfProcessInfo.setThresholdValue(tfIdfRequestInfo.getThresholdValue());
                tfIdfProcessInfo.setWordElimination(wordElimination);
                applicationContext.getBean(TfIdfInitializerService.class).saveAndSendToQueue(tfIdfProcessInfo);

                if (thValue.compareTo(tfIdfRequestInfo.getThresholdValue()) == 0) {
                    result = false;
                    break;
                }
            }
        }
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
        }catch (final Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}