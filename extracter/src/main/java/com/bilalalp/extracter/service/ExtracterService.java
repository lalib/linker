package com.bilalalp.extracter.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.site.SiteInfoType;

public interface ExtracterService {

    void parse(PatentInfo patentInfo);

    SiteInfoType getSiteInfoType();
}
