package com.bilalalp.common.service;

import com.bilalalp.common.entity.site.SiteInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.repository.SiteInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SiteInfoServiceImpl implements SiteInfoService {

    @Autowired
    private SiteInfoRepository siteInfoRepository;

    @Transactional
    @Override
    public void persistIfNotExist(final List<SiteInfo> initialSiteInfoList) {

        for (final SiteInfo siteInfo : initialSiteInfoList) {
            final SiteInfoType siteInfoType = siteInfo.getSiteInfoType();

            final SiteInfo foundSiteInfo = siteInfoRepository.getSiteInfoBySiteInfoType(siteInfoType);

            if (foundSiteInfo == null) {
                siteInfoRepository.save(siteInfo);
            }
        }
    }
}