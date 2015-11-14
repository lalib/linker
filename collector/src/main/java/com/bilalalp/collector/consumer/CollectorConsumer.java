package com.bilalalp.collector.consumer;

import com.bilalalp.collector.service.CollectorService;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.service.PatentInfoService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectorConsumer implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private CollectorService collectorService;

    @Transactional
    @Override
    public void onMessage(final Message message) {

        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println(queueMessageDto.getId().toString());

        final PatentInfo patentInfo = patentInfoService.find(queueMessageDto.getId());
        collectorService.collect(patentInfo);
    }
}