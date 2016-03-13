package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import com.bilalalp.common.service.base.BaseService;

public interface ClusterResultInfoService extends BaseService<ClusteringResultInfo> {

    void saveInNewTransaction(ClusteringResultInfo clusteringResultInfo);
}
