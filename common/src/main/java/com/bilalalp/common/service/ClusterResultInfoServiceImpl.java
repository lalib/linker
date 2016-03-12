package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusterResultInfo;
import com.bilalalp.common.repository.ClusterResultInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
public class ClusterResultInfoServiceImpl extends AbstractService<ClusterResultInfo> implements ClusterResultInfoService {

    @Autowired
    private ClusterResultInfoRepository repository;

    @Transactional
    @Override
    public void saveInNewTransaction(final ClusterResultInfo clusterResultInfo) {
        save(clusterResultInfo);
    }
}