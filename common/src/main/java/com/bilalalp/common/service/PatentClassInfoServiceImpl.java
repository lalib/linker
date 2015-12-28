package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.PatentClassInfo;
import com.bilalalp.common.repository.PatentClassInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class PatentClassInfoServiceImpl extends AbstractService<PatentClassInfo> implements PatentClassInfoService {

    @Autowired
    private PatentClassInfoRepository patentClassInfoRepository;

    @Override
    protected CrudRepository<PatentClassInfo, Long> getRepository() {
        return patentClassInfoRepository;
    }

    @Override
    public void deleteAllPatentInfoClasses(final Long requestId) {
        patentClassInfoRepository.deleteAllPatentInfoClasses(requestId);
    }
}
