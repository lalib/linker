package com.bilalalp.searcher.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class FoundLinkConsumer implements MessageListener {

    @Override
    public void onMessage(final Message message) {
        System.out.println(message);
    }
}
