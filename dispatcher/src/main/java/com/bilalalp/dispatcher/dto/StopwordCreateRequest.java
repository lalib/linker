package com.bilalalp.dispatcher.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class StopWordCreateRequest {

    @XmlElement(name = "stopWord")
    @XmlElementWrapper
    private List<String> stopWordList;
}