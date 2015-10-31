package com.bilalalp.common.config;

import com.bilalalp.common.service.SiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class LinkerInitializer {

    @Autowired
    private SiteInfoService siteInfoService;

    @PostConstruct
    public void init() {
        siteInfoService.persistIfNotExist(PatentSearcherInitialData.getInitialSiteInfoList());
    }
}
