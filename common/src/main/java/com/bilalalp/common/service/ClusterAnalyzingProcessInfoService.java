package com.bilalalp.common.service;


import com.bilalalp.common.entity.cluster.ClusterAnalyzingProcessInfo;
import com.bilalalp.common.service.base.BaseService;

public interface ClusterAnalyzingProcessInfoService extends BaseService<ClusterAnalyzingProcessInfo> {

    void saveInNewTransaction(ClusterAnalyzingProcessInfo clusterAnalyzingProcessInfo);
}