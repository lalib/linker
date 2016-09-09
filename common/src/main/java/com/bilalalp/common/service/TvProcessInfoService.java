package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.SplitWordType;
import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TvProcessInfoService extends BaseService<TvProcessInfo> {
    List<TvProcessInfo> findByLimitWithoutAdditionalWords(Long count);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveInNewTransaction(List<TvProcessInfo> tvProcessInfoList);

    List<TvProcessInfo> findByLimit(int limitCount);

    List<TvProcessInfo> findByLimit(int limitCount, List<Long> patentIds);

    List<TvProcessInfo> findByLimit(int limitCount, SplitWordType splitWordType,List<Long> patentIds);
}
