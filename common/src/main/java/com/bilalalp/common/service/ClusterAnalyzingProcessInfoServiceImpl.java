package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusterAnalyzingProcessInfo;
import com.bilalalp.common.repository.ClusterAnalyzingProcessInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
public class ClusterAnalyzingProcessInfoServiceImpl extends AbstractService<ClusterAnalyzingProcessInfo> implements ClusterAnalyzingProcessInfoService {

    @Autowired
    private ClusterAnalyzingProcessInfoRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveInNewTransaction(final ClusterAnalyzingProcessInfo clusterAnalyzingProcessInfo) {
        save(clusterAnalyzingProcessInfo);
    }
}