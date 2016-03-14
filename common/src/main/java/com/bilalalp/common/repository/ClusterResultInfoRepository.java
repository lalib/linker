package com.bilalalp.common.repository;


import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface ClusterResultInfoRepository extends CrudRepository<ClusteringResultInfo, Long> ,Serializable{

}
