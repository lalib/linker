package com.bilalalp.common.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.repository.PatentInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class PatentInfoServiceImpl extends AbstractService<PatentInfo> implements PatentInfoService {

    @Autowired
    private PatentInfoRepository patentInfoRepository;

    @Override
    protected CrudRepository<PatentInfo, Long> getRepository() {
        return patentInfoRepository;
    }
}