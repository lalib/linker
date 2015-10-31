package com.bilalalp.dispatcher.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "queueMessageDto")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queueMessageDto", propOrder = {
        "message",
        "id"
})
@Setter
@Getter
@NoArgsConstructor
public class QueueMessageDto {

    private String message;

    private Long id;

    public QueueMessageDto(final Long id) {
        setId(id);
    }
}