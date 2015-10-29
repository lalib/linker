package com.bilalalp.dispatcher.dto;


import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class LinkSearchResponse {

    private String message;
}