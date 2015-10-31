package com.bilalalp.common.service;

import com.bilalalp.common.entity.site.SiteInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.repository.SiteInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SiteInfoServiceImpl extends AbstractService<SiteInfo> implements SiteInfoService {

    @Autowired
    private SiteInfoRepository siteInfoRepository;


    @Override
    protected CrudRepository<SiteInfo, Long> getRepository() {
        return siteInfoRepository;
    }

    @Transactional
    @Override
    public void persistIfNotExist(final List<SiteInfo> initialSiteInfoList) {

        for (final SiteInfo siteInfo : initialSiteInfoList) {
            final SiteInfoType siteInfoType = siteInfo.getSiteInfoType();

            final SiteInfo foundSiteInfo = getSiteInfoBySiteInfoType(siteInfoType);

            if (foundSiteInfo == null) {
                save(siteInfo);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public SiteInfo getSiteInfoBySiteInfoType(final SiteInfoType siteInfoType) {
        return siteInfoRepository.getSiteInfoBySiteInfoType(siteInfoType);
    }
}