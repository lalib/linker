package com.bilalalp.tfidfprocessor.amqp;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageSenderImpl implements MessageSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Transactional
    @Override
    public void sendMessage(final QueueConfigurationDto queueConfigurationDto, final QueueMessageDto queueMessageDto) {
        amqpTemplate.convertAndSend(queueConfigurationDto.getExchangeName(), queueConfigurationDto.getQueueKey(), queueMessageDto);
    }

    @Transactional
    @Override
    public void sendMessage(final QueueConfigurationDto queueConfigurationDto, final List<QueueMessageDto> queueMessageDtoList) {

        for (final QueueMessageDto queueMessageDto : queueMessageDtoList) {
            sendMessage(queueConfigurationDto, queueMessageDto);
        }
    }
}