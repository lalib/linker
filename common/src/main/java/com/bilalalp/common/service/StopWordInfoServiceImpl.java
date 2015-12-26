package com.bilalalp.common.service;

import com.bilalalp.common.entity.StopWordInfo;
import com.bilalalp.common.repository.StopWordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class StopWordInfoServiceImpl extends AbstractService<StopWordInfo> implements StopWordInfoService {

    @Autowired
    private StopWordInfoRepository stopWordInfoRepository;

    @Override
    protected CrudRepository<StopWordInfo, Long> getRepository() {
        return stopWordInfoRepository;
    }
}