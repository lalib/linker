package com.bilalalp.dispatcher.validation;

import com.bilalalp.common.exception.LinkerCommonException;
import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class LinkSearchRequestValidator implements Validator<LinkSearchRequest> {

    @Override
    public void validate(final LinkSearchRequest linkSearchRequest) {

        if (CollectionUtils.isEmpty(linkSearchRequest.getKeywordList())) {
            throw new LinkerCommonException("Keywords can not be null!");
        }

        if (CollectionUtils.isEmpty(linkSearchRequest.getSiteInfoTypeList())) {
            throw new LinkerCommonException("SiteInfo can not be null!");
        }
    }
}