package com.bilalalp.common.service;

import com.bilalalp.common.entity.site.SiteInfo;

import java.util.List;

public interface SiteInfoService {

    void persistIfNotExist(List<SiteInfo> initialSiteInfoList);
}
