package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import com.bilalalp.common.service.base.BaseService;

import java.io.Serializable;

public interface ClusterResultInfoService extends BaseService<ClusteringResultInfo>, Serializable {

    Long getPatentCountWithOutClusterNumber(Long clusterNumber);

    void saveInNewTransaction(ClusteringResultInfo clusteringResultInfo);
}