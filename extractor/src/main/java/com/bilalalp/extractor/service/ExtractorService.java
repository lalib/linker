package com.bilalalp.extractor.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.site.SiteInfoType;

public interface ExtractorService {

    void parse(PatentInfo patentInfo);

    SiteInfoType getSiteInfoType();
}
