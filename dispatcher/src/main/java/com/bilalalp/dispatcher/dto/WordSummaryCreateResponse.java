package com.bilalalp.dispatcher.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class WordSummaryCreateResponse {

    private boolean success = true;
}