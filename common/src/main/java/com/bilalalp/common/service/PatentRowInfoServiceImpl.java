package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.PatentRowInfo;
import com.bilalalp.common.repository.PatentRowInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
public class PatentRowInfoServiceImpl extends AbstractService<PatentRowInfo> implements PatentRowInfoService {

    @Autowired
    private PatentRowInfoRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveInNewTransaction(final PatentRowInfo patentRowInfo) {
        repository.save(patentRowInfo);
    }
}
