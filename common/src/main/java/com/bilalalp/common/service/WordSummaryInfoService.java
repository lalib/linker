package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.WordSummaryInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.service.base.BaseService;

public interface WordSummaryInfoService extends BaseService<WordSummaryInfo> {

    void bulkInsert(final LinkSearchRequestInfo linkSearchRequestInfo);
}
