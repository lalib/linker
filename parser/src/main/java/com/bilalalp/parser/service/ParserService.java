package com.bilalalp.parser.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.site.SiteInfoType;

public interface ParserService {

    void parse(PatentInfo patentInfo);

    SiteInfoType getSiteInfoType();
}
