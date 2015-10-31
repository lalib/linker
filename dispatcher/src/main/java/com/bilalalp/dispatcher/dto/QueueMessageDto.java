package com.bilalalp.dispatcher.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueueMessageDto {

    private String message;

    private Long id;
}