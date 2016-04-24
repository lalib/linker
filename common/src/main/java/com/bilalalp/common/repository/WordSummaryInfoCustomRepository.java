package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface WordSummaryInfoCustomRepository {

    void bulkInsert(LinkSearchRequestInfo linkSearchRequestInfo);

    @Transactional(propagation = Propagation.SUPPORTS)
    List<Long> getWordIds(Long lsrId, List<String> wordList);

    List<BigDecimal> getTvWordIds();

    List<TvProcessInfo> getTfResult(Long patentId, Long lsrId, Long patentCount);
}