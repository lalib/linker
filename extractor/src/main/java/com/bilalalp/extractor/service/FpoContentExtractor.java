package com.bilalalp.extractor.service;

import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Date;

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
                            return abstractContent.get(0).text().trim();
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