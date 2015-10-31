package com.bilalalp.dispatcher.amqp;

import com.bilalalp.dispatcher.dto.QueueConfigurationDto;
import com.bilalalp.dispatcher.dto.QueueMessageDto;

public interface MessageSender {

    void sendMessage(final QueueConfigurationDto queueConfigurationDto, final QueueMessageDto queueMessageDto);
}