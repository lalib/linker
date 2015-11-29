package com.bilalalp.parser.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.PatentInfoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsptoContentParser implements ParserService {

    @Autowired
    private PatentInfoService patentInfoService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void parse(final PatentInfo patentInfo) {

        final String body = patentInfo.getBody();
        final Document documentBody = Jsoup.parse(body);

        final String abstractContent = getAbstractContent(documentBody);
        patentInfo.setAbstractContent(abstractContent);
        patentInfoService.save(patentInfo);
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.USPTO;
    }

    private String getAbstractContent(final Document document) {

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
            return null;
        }
    }
}