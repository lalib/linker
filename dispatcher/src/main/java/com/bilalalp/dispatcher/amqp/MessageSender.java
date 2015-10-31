package com.bilalalp.dispatcher.amqp;

import com.bilalalp.dispatcher.dto.QueueConfigurationDto;
import com.bilalalp.dispatcher.dto.QueueMessageDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Transactional
    public void sendMessage(final QueueConfigurationDto queueConfigurationDto, final QueueMessageDto queueMessageDto) {
        amqpTemplate.convertAndSend(queueConfigurationDto.getExchangeName(), queueConfigurationDto.getQueueKey(), queueMessageDto);
    }
}