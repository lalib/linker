package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.TvResultInfo;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TvResultInfoService extends BaseService<TvResultInfo> {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveInNewTransaction(TvResultInfo tvResultInfo);

    TvResultInfo getByWordId(long l);
}
