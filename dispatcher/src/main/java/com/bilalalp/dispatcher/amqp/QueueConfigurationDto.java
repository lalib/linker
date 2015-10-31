package com.bilalalp.dispatcher.amqp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueConfigurationDto {

    private String exchangeName;

    private String queueName;

    private String queueKey;
}