package com.bilalalp.common.service;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.service.base.BaseService;

import java.util.List;

public interface PatentInfoService extends BaseService<PatentInfo> {

    List<PatentInfo> getPatentListBylinkSearchPageInfo(LinkSearchPageInfo linkSearchPageInfo);

    List<Long> getPatentIds(Long requestId);

    void resetParseInformation(Long requestId);

    Long getPatentInfoCountByLinkSearchPageInfo(final LinkSearchRequestInfo linkSearchRequestInfo);

    List<EntityDto> getPatentInfos(Long lsrId, Long word);
}