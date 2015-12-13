package com.bilalalp.extractor.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.service.PatentInfoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractExtractorService implements ExtractorService {

    @Autowired
    private PatentInfoService patentInfoService;

    @Transactional(propagation = Propagation.SUPPORTS)
    public void parse(final PatentInfo patentInfo) {

        final String body = patentInfo.getBody();
        final Document documentBody = Jsoup.parse(body);
        final String abstractContent = getAbstractContent(documentBody);
        final String claimContent = getClaimContent(documentBody);
        final String descriptionContent = getDescriptionContent(documentBody);

        patentInfo.setAbstractContent(abstractContent);
        patentInfo.setClaimContent(claimContent);
        patentInfo.setDescriptionContent(descriptionContent);
        patentInfoService.save(patentInfo);
    }

    protected abstract String getAbstractContent(Document documentBody);

    protected abstract String getClaimContent(Document document);

    protected abstract String getDescriptionContent(Document document);
}