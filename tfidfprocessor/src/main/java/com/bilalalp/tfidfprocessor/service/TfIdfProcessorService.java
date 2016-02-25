package com.bilalalp.tfidfprocessor.service;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.tfidf.TfIdfProcessInfo;
import com.bilalalp.common.service.TfIdfInfoService;
import com.bilalalp.common.service.TfIdfProcessInfoService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TfIdfProcessorService implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private TfIdfInfoService tfIdfInfoService;

    @Autowired
    private TfIdfProcessInfoService tfIdfProcessInfoService;

    @Transactional
    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println(queueMessageDto.getId());
        final TfIdfProcessInfo tfIdfProcessInfo = tfIdfProcessInfoService.find(queueMessageDto.getId());
        tfIdfInfoService.processEliminatedWord(tfIdfProcessInfo.getLinkSearchRequestInfo(), tfIdfProcessInfo.getWordElimination(), tfIdfProcessInfo.getThresholdValue());
    }
}