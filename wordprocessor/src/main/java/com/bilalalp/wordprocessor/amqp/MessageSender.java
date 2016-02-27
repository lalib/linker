package com.bilalalp.wordprocessor.amqp;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;

import java.util.List;

public interface MessageSender {

    void sendMessage(final QueueConfigurationDto queueConfigurationDto, final QueueMessageDto queueMessageDto);

    void sendMessage(final QueueConfigurationDto queueConfigurationDto, final List<QueueMessageDto> queueMessageDtoList);
}