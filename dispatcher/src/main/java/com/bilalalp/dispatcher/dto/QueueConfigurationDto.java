package com.bilalalp.dispatcher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueConfigurationDto {

    private String exchangeName;

    private String queueName;

    private String queueKey;
}