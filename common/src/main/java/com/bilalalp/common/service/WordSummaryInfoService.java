package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.WordSummaryInfo;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WordSummaryInfoService extends BaseService<WordSummaryInfo> {

    void bulkInsert(final LinkSearchRequestInfo linkSearchRequestInfo);

    List<WordSummaryInfo> findByLinkSearchRequestInfo(LinkSearchRequestInfo linkSearchRequestInfo, Pageable pageable);

    WordSummaryInfo findByLinkSearchRequestInfoAndWord(LinkSearchRequestInfo linkSearchRequestInfo, String word);
}