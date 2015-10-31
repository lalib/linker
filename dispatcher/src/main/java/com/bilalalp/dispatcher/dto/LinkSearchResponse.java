package com.bilalalp.dispatcher.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@XmlRootElement
public class LinkSearchResponse {

    private Long searchId;
}