package com.bilalalp.dispatcher.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class DispatcherRequestErrorListener implements MessageListener {

    @Override
    public void onMessage(final Message message) {

    }
}