package com.bilalalp.extractor.service;

import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class FpoContentExtractor extends AbstractExtractorService implements ExtractorService {

    @Override
    protected String getInternationalClass(final Document document) {
        return getContent(document, "International Classes:");
    }

    @Override
    protected String getPrimaryClass(final Document document) {
        return getContent(document, "Primary Class:");
    }

    @Override
    protected Date getPublicationDate(final Document document) {
        final String content = getContent(document, "Publication Date:");
        return StringUtils.isNotEmpty(content) ? DateUtil.toDate(content, DateUtil.DD_MM_YYYY) : null;
    }

    @Override
    protected Date getFillingDate(final Document document) {
        final String content = getContent(document, "Filing Date:");
        return StringUtils.isNotEmpty(content) ? DateUtil.toDate(content, DateUtil.DD_MM_YYYY) : null;
    }

    @Override
    protected String getAssignee(final Document document) {
        return getContent(document, "Assignee:");
    }

    @Override
    protected String getApplicationNumber(final Document document) {
        return getContent(document, "Application Number:");
    }

    @Override
    protected String getInventors(final Document document) {
        return getContent(document, "Inventors:");
    }

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

    @Override
    protected String getPatentNumber(Document document) {
        try {

            final Element body = document.body();
            final Elements elementsContainingOwnText = body.getElementsByAttributeValue("style", "clear: none;");

            if (elementsContainingOwnText != null && !elementsContainingOwnText.isEmpty()) {
                return getString(elementsContainingOwnText);
            } else {
                return getContent(document, "Document Type and Number:");
            }
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private String getString(final Elements elementsContainingOwnText) {
        final Elements allElements = elementsContainingOwnText.get(0).getAllElements();
        if (allElements.size() == 1) {
            return allElements.get(0).text();
        } else if (allElements.size() > 1) {
            return allElements.get(1).text();
        } else {
            return null;
        }
    }

    private String getContent(final Document document, final String searchText) {
        try {

            final Element body = document.body();
            final Elements elementsContainingOwnText = body.getElementsContainingOwnText(searchText);

            if (elementsContainingOwnText != null) {
                return getString(searchText, elementsContainingOwnText);
            } else {
                return null;
            }
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private String getString(final String searchText, final Elements elementsContainingOwnText) {
        for (final Element element : elementsContainingOwnText) {
            if (element.text().contains(searchText)) {
                final Element parent = element.parent();
                final Elements abstractContent = parent.getElementsByClass("disp_elm_text");
                if (abstractContent != null && !abstractContent.isEmpty()) {
                    return abstractContent.get(0).text().trim();
                }
            }
        }
        return null;
    }

    @Override
    public SiteInfoType getSiteInfoType() {
        return SiteInfoType.FPO;
    }
}