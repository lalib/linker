package com.bilalalp.common.service;


import com.bilalalp.common.entity.patent.WordElimination;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface WordEliminationService extends BaseService<WordElimination> {

    void process(Long lsrId, Long thresholdValue);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveWithNewTransaction(WordElimination wordElimination);
}
