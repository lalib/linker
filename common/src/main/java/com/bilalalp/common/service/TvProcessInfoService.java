package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TvProcessInfoService extends BaseService<TvProcessInfo> {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveInNewTransaction(List<TvProcessInfo> tvProcessInfoList);

    List<TvProcessInfo> findByLimit(int limitCount);
}
