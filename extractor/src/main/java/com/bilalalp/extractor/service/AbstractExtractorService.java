package com.bilalalp.extractor.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.service.PatentInfoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
        final String inventors = getInventors(documentBody);
        final String assignee=getAssignee(documentBody);
        final String primaryClass = getPrimaryClass(documentBody);
        final Date publicationDate = getPublicationDate(documentBody);
        final String internationalClass = getInternationalClass(documentBody);
        final Date fillingDate = getFillingDate(documentBody);
        final String applicationNumber = getApplicationNumber(documentBody);

        patentInfo.setApplicationNumber(applicationNumber);
        patentInfo.setFillingDate(fillingDate);
        patentInfo.setInternationalClass(internationalClass);
        patentInfo.setPublicationDate(publicationDate);
        patentInfo.setPrimaryClass(primaryClass);
        patentInfo.setAssignee(assignee);
        patentInfo.setAbstractContent(abstractContent);
        patentInfo.setClaimContent(claimContent);
        patentInfo.setDescriptionContent(descriptionContent);
        patentInfo.setInventors(inventors);
        patentInfoService.save(patentInfo);
    }

    protected abstract String getInternationalClass(Document document);

    protected abstract String getPrimaryClass(Document document);

    protected abstract Date getPublicationDate(Document document);

    protected abstract Date getFillingDate(Document document);

    protected abstract String getAssignee(Document document);

    protected abstract String getApplicationNumber(Document document);

    protected abstract String getInventors(Document document);

    protected abstract String getAbstractContent(Document documentBody);

    protected abstract String getClaimContent(Document document);

    protected abstract String getDescriptionContent(Document document);
}