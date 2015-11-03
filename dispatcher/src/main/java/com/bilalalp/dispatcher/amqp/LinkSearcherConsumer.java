package com.bilalalp.dispatcher.amqp;

import com.bilalalp.common.dto.QueueMessageDto;
import lombok.Setter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Setter
@Service
public class LinkSearcherConsumer implements MessageListener{

    @Autowired
    private MessageConverter messageConverter;

    @Override
    public void onMessage(Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println(queueMessageDto.getId().toString());
    }
}