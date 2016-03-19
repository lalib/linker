package com.bilalalp.common.repository;


import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface ClusterResultInfoRepository extends CrudRepository<ClusteringResultInfo, Long> ,Serializable{

    @Query("SELECT COUNT(p) FROM ClusteringResultInfo p WHERE p.clusteringNumber != :clusterNumber")
    Long getPatentCountWithOutClusterNumber(@Param("clusterNumber") Long clusterNumber);
}
