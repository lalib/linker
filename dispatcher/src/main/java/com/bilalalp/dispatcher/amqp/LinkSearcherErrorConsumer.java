package com.bilalalp.dispatcher.amqp;

import lombok.Setter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Setter
@Service
public class LinkSearcherErrorConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {

    }
}
