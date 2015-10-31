package com.bilalalp.common.service;

import com.bilalalp.common.entity.site.SiteInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.base.BaseService;

import java.util.List;

public interface SiteInfoService extends BaseService<SiteInfo> {

    void persistIfNotExist(List<SiteInfo> initialSiteInfoList);

    SiteInfo getSiteInfoBySiteInfoType(SiteInfoType siteInfoType);
}
