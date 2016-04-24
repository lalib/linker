package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.TvResultInfo;
import com.bilalalp.common.repository.TvResultInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Getter
@Service
public class TvResultInfoServiceImpl extends AbstractService<TvResultInfo> implements TvResultInfoService, Serializable {

    @Autowired
    private TvResultInfoRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveInNewTransaction(final TvResultInfo tvResultInfo) {
        repository.save(tvResultInfo);
    }

    @Override
    public TvResultInfo getByWordId(long l) {
        return repository.findByWordId(l);
    }
}