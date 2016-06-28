package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.SplitWordType;
import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import com.bilalalp.common.repository.TvProcessInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Service
public class TvProcessInfoServiceImpl extends AbstractService<TvProcessInfo> implements TvProcessInfoService {

    @Autowired
    private TvProcessInfoRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveInNewTransaction(final List<TvProcessInfo> tvProcessInfoList) {
        save(tvProcessInfoList);
    }

    @Override
    public List<TvProcessInfo> findByLimit(final int limitCount) {
        return repository.findByLimit(new PageRequest(0, limitCount));
    }

    @Override
    public List<TvProcessInfo> findByLimit(final int limitCount, final List<Long> patentIds) {
        return repository.findByLimit(new PageRequest(0, limitCount), patentIds);
    }

    @Override
    public List<TvProcessInfo> findByLimit(final int limitCount, final SplitWordType splitWordType,final List<Long> patentIds) {
        return repository.findByLimit(new PageRequest(0, limitCount), splitWordType,patentIds);
    }
}