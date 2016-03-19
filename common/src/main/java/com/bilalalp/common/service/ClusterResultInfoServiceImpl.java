package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import com.bilalalp.common.repository.ClusterResultInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Getter
@Service
public class ClusterResultInfoServiceImpl extends AbstractService<ClusteringResultInfo> implements ClusterResultInfoService, Serializable {

    @Autowired
    private ClusterResultInfoRepository repository;

    @Override
    public Long getPatentCountWithOutClusterNumber(final Long clusterNumber) {
        return repository.getPatentCountWithOutClusterNumber(clusterNumber);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveInNewTransaction(final ClusteringResultInfo clusteringResultInfo) {
        save(clusteringResultInfo);
    }
}