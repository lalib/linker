package com.bilalalp.common.service;


import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.WordElimination;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WordEliminationService extends BaseService<WordElimination> {

    void process(Long lsrId, Long thresholdValue);

    void saveWithNewTransaction(WordElimination wordElimination);

    List<WordElimination> getEliminatedWordsByLinkSearchRequestInfoAndThresholdValue(LinkSearchRequestInfo linkSearchRequestInfo, Pageable pageable);
}