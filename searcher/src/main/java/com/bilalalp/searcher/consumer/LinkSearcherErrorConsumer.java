package com.bilalalp.searcher.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class LinkSearcherErrorConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {

    }
}