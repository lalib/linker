package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordSummaryInfoCustomRepository {

    void bulkInsert(LinkSearchRequestInfo linkSearchRequestInfo);

    @Transactional(propagation = Propagation.SUPPORTS)
    List<Long> getWordIds(Long lsrId, List<String> wordList);
}