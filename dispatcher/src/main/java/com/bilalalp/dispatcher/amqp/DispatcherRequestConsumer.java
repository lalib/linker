package com.bilalalp.dispatcher.amqp;

import com.bilalalp.dispatcher.dto.QueueMessageDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispatcherRequestConsumer implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println("GELDÝ");
        System.out.println(queueMessageDto.getId().toString());
    }
}