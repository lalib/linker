package com.bilalalp.dispatcher.dto;

import com.bilalalp.common.entity.site.SiteInfoType;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class LinkSearchRequest {

    @XmlElement(name = "siteInfoTypeList")
    @XmlElementWrapper
    private List<SiteInfoType> siteInfoTypeList;

    @XmlElement(name = "keywordList")
    @XmlElementWrapper
    private List<String> keywordList;
}