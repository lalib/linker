package com.bilalalp.common.repository;

import com.bilalalp.common.entity.site.SiteInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteInfoRepository extends CrudRepository<SiteInfo, Long> {

    SiteInfo getSiteInfoBySiteInfoType(SiteInfoType siteInfoType);
}