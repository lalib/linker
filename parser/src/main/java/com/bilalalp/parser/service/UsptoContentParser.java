package com.bilalalp.parser.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsptoContentParser implements ParserService {

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void parse(final PatentInfo patentInfo) {

        System.out.println(patentInfo);
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.USPTO;
    }
}