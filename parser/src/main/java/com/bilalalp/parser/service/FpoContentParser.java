package com.bilalalp.parser.service;

import com.bilalalp.common.entity.site.SiteInfoType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class FpoContentParser extends AbstractParserService implements ParserService {

    @Override
    protected String getAbstractContent(final Document documentBody) {
        return getContent(documentBody, "Abstract:");
    }

    @Override
    protected String getClaimContent(final Document document) {
        return getContent(document, "Claims:");
    }

    @Override
    protected String getDescriptionContent(final Document document) {
        return getContent(document, "Description:");
    }

    private String getContent(final Document document, final String searchText) {
        try {

            final Element body = document.body();
            final Elements elementsContainingOwnText = body.getElementsContainingOwnText(searchText);

            if (elementsContainingOwnText != null) {
                for (final Element element : elementsContainingOwnText) {
                    if (element.text().contains(searchText)) {
                        final Element parent = element.parent();
                        final Elements abstractContent = parent.getElementsByClass("disp_elm_text");
                        if (abstractContent != null && !abstractContent.isEmpty()) {
                            return abstractContent.get(0).text();
                        }
                    }
                }
            }
            return null;
        } catch (final Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.FPO;
    }
}