package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WordSummaryInfoService extends BaseService<WordSummaryInfo> {

    Long getCountByLinkSearchRequestInfoAndThresholdValue(LinkSearchRequestInfo linkSearchRequestInfo, Long thresholdValue);

    List<WordSummaryInfo> findByLinkSearchRequestInfoAndThresholdValue(LinkSearchRequestInfo linkSearchRequestInfo, Long thresholdValue, Pageable pageable);

    void bulkInsert(final LinkSearchRequestInfo linkSearchRequestInfo);

    List<WordSummaryInfo> findByLinkSearchRequestInfo(LinkSearchRequestInfo linkSearchRequestInfo, Pageable pageable);

    WordSummaryInfo findByLinkSearchRequestInfoAndWord(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);

    List<Long> getWordIds(Long lsrId, List<String> wordList);
}