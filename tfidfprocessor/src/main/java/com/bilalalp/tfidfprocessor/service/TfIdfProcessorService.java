package com.bilalalp.tfidfprocessor.service;

import com.bilalalp.common.dto.QueueMessageDto;
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

    @Transactional
    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println(queueMessageDto.getId());
    }
}