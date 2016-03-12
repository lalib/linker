package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusterResultInfo;
import com.bilalalp.common.service.base.BaseService;

public interface ClusterResultInfoService extends BaseService<ClusterResultInfo> {

    void saveInNewTransaction(ClusterResultInfo clusterResultInfo);
}
