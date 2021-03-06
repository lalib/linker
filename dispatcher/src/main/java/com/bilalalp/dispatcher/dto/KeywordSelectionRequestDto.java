package com.bilalalp.dispatcher.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class KeywordSelectionRequestDto {

    @XmlElement(name = "firstRequestId")
    private Long firstRequestId;

    @XmlElement(name = "secondRequestId")
    private Long secondRequestId;

    @XmlElement(name = "topKeywordSelectionCount")
    private Long topKeywordSelectionCount;

    @XmlElement(name = "ratio")
    private Double ratio;
}