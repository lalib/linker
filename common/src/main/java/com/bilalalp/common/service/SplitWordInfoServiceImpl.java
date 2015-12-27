package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.SplitWordInfo;
import com.bilalalp.common.repository.SplitWordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class SplitWordInfoServiceImpl extends AbstractService<SplitWordInfo> implements SplitWordInfoService {

    @Autowired
    private SplitWordInfoRepository splitWordInfoRepository;

    @Override
    protected CrudRepository<SplitWordInfo, Long> getRepository() {
        return splitWordInfoRepository;
    }
}