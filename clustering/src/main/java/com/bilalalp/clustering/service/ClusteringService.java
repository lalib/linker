package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;

import java.io.Serializable;

public interface ClusteringService extends Serializable{

    void cluster(ClusteringRequestInfo clusteringRequestInfo);
}