package com.bilalalp.extractor.service;

import com.bilalalp.common.entity.site.SiteInfoType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UsptoContentExtractor extends AbstractExtractorService implements ExtractorService {

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.USPTO;
    }

    @Override
    protected String getInternationalClass(Document document) {
        return null;
    }

    @Override
    protected String getPrimaryClass(Document document) {
        return null;
    }

    @Override
    protected Date getPublicationDate(Document document) {
        return null;
    }

    @Override
    protected Date getFillingDate(Document document) {
        return null;
    }

    @Override
    protected String getAssignee(Document document) {
        return null;
    }

    @Override
    protected String getApplicationNumber(Document document) {
        return null;
    }

    @Override
    protected String getInventors(Document document) {


        return null;
    }

    @Override
    protected String getAbstractContent(final Document document) {

        try {
            final Element body = document.body();
            final Elements allElements = body.getAllElements();

            boolean found = false;

            for (final Element element : allElements) {

                if (found) {
                    return element.text();
                }

                if ("b".equals(element.tagName()) && "Abstract".equals(element.text())) {
                    found = true;
                }
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected String getClaimContent(final Document document) {
        System.out.println("Not implemented Yet!");
        throw new RuntimeException("Not implemented Yet!");
    }

    @Override
    protected String getDescriptionContent(Document document) {
        System.out.println("Not implemented Yet!");
        throw new RuntimeException("Not implemented Yet!");
    }

    @Override
    protected String getPatentNumber(Document document) {
        return null;
    }
}