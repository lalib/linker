package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.PatentClassInfo;
import com.bilalalp.common.repository.PatentClassInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Getter
@Service
public class PatentClassInfoServiceImpl extends AbstractService<PatentClassInfo> implements PatentClassInfoService {

    @Autowired
    private PatentClassInfoRepository repository;

    @Override
    public void deleteAllPatentInfoClasses(final Long requestId) {
        repository.deleteAllPatentInfoClasses(requestId);
    }
}
